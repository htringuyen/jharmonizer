package io.javaside.jharmonizer.internal;

import io.javaside.jharmonizer.*;
import io.javaside.jharmonizer.common.ConfigDef;
import io.javaside.jharmonizer.common.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static io.javaside.jharmonizer.common.MiscUtils.loadObject;
import static io.javaside.jharmonizer.common.MiscUtils.stringValueFrom;

public class DefaultHarmonizer implements Harmonizer {

    private static final Logger logger = LoggerFactory.getLogger(DefaultHarmonizer.class);

    public static final ConfigDef MERGING_RESOLVER_CLASS_DEF = ConfigDef.of("merging.resolver.class", DefaultMergingResolver.class.getName());

    public static final ConfigDef SOURCE_LIST_DEF = ConfigDef.of("source.list", true);

    public static final ConfigDef HARMONIZATION_LISTENER_CLASS_DEF = ConfigDef.of("listener.class", "");

    public static final ConfigDef HARMONIZATION_LISTENER_NAME_DEF = ConfigDef.of("listener.name", "");

    public static final ConfigDef MAX_SOURCE_QUEUE_SIZE_DEF = ConfigDef.of("max.source.queue.size", "4096");

    public static final ConfigDef MERGE_PUSH_INTERVAL_MS_DEF = ConfigDef.of("merge.push.interval.ms", "1000");

    public static final String MERGING_RESOLVER_CONF_PREFIX = "merging.resolver";

    public static final String SOURCE_CONF_PREFIX = "source";

    public static final String HARMONIZATION_LISTENER_CONF_PREFIX = "listener";

    private List<SourceTask> sourceTasks;

    private MergingResolver mergingResolver;

    private BlockingQueue<SourceRecord<?, ?>> sourceQueue;

    private long mergePushIntervalMs;

    private ExecutorService taskExecutor;

    private ScheduledExecutorService pushingExecutor;

    private final List<HarmonizationListener<?, ?>> listeners = new ArrayList<>();

    private Thread harmonizerThread;

    private AtomicInteger sourceTaskCompletionCountDown;

    private DefaultHarmonizer(HarmonizationListener<?, ?>... listeners) {
        this.listeners.addAll(List.of(listeners));
    }

    @Override
    public void configure(Properties props) {

        logProperties(props);

        var config = Configuration.from(props);

        var mergingResolverClassName = stringValueFrom(props, MERGING_RESOLVER_CLASS_DEF);
        mergingResolver = loadObject(mergingResolverClassName, MergingResolver.class);
        mergingResolver.configure(config.subset(MERGING_RESOLVER_CONF_PREFIX, true).asProperties());

        sourceTasks = parseSourceList(stringValueFrom(props, SOURCE_LIST_DEF)).stream()
                .map(sourceName -> createSourceTask(sourceName, config))
                .toList();

        var maxQueueSize = Integer.parseInt(stringValueFrom(props, MAX_SOURCE_QUEUE_SIZE_DEF));
        sourceQueue = new LinkedBlockingQueue<>(maxQueueSize);

        mergePushIntervalMs = Long.parseLong(stringValueFrom(props, MERGE_PUSH_INTERVAL_MS_DEF));

        var listenerClassName = stringValueFrom(props, HARMONIZATION_LISTENER_CLASS_DEF);
        if (!listenerClassName.isBlank()) {
            var listenerName = stringValueFrom(props, HARMONIZATION_LISTENER_NAME_DEF);
            var listener = loadObject(listenerClassName, HarmonizationListener.class);
            listener.configure(config.subset(true, HARMONIZATION_LISTENER_CONF_PREFIX, listenerName).asProperties());
            listeners.add(listener);
        }

        listeners.forEach(mergingResolver::addListener);
    }

    @Override
    public void run() {
        if (harmonizerThread != null) {
            throw new IllegalStateException("Harmonizer is run previously. Please dont try to reuse Harmonizer.");
        }

        logger.debug("Harmonizer started...");

        harmonizerThread = Thread.currentThread();
        taskExecutor = Executors.newCachedThreadPool();
        pushingExecutor = Executors.newScheduledThreadPool(1);

        initSourceTaskCompletionCountDown(sourceTasks.size());
        sourceTasks.forEach(t -> taskExecutor.submit(() -> runSourceTask(t)));

        pushingExecutor.scheduleAtFixedRate(this::runMergePusher,
                mergePushIntervalMs, mergePushIntervalMs, TimeUnit.MILLISECONDS);

        final AtomicBoolean shouldContinue = new AtomicBoolean(true);

        while (shouldContinue.get()) {
            try {
                //logger.debug("Waiting for source record");
                var record = sourceQueue.take();
                //logger.debug("Processing record: {}", record.getValue().toString());
                mergingResolver.accept(record);
            } catch (InterruptedException e) {
                shouldContinue.set(false);
            }
        }

        sourceQueue.forEach(mergingResolver::accept);
        while (!mergingResolver.tryPushAllMerges()) {
            sleepMs(mergePushIntervalMs);
            logger.debug("Source tasks completed, try pushing merges, remaining {}...", mergingResolver.getMergeCount());
        }
    }

    private void runSourceTask(SourceTask task) {
        try {
            task.run();
            markSourceTaskCompletion();
        }
        catch (Exception e) {
            logger.error("Error running source task", e);
            close();
        }
    }

    private void runMergePusher() {
        try {
            mergingResolver.tryPushAllMerges();
        } catch (Exception e) {
            logger.warn("Error pushing merges...", e);
        }
    }

    private void initSourceTaskCompletionCountDown(int numTasks) {
        sourceTaskCompletionCountDown = new AtomicInteger(numTasks);
    }

    private void markSourceTaskCompletion() {
        if (sourceTaskCompletionCountDown.decrementAndGet() == 0) {
            halfClose();

        }
    }

    private void halfClose() {
        harmonizerThread.interrupt();
        stopExecutor(pushingExecutor);
    }

    @Override
    public void close() {

        if (harmonizerThread == null) return;

        halfClose();
        stopExecutor(taskExecutor);
        logger.debug("Harmonizer stopped...");
    }

    private void stopExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                throw new RuntimeException("Executor shutdown timed out");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Executor shutdown interrupted", e);
        }
    }

    private void consumeSourceRecord(SourceRecord<?, ?> record) {
        try {
            //logger.debug("Putting record into source queue: {}", record.getValue().toString());
            sourceQueue.put(record);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private SourceTask createSourceTask(String sourceName, Configuration hConfig) {
        var sourceTask = SourceTask.newInstance(sourceName, this::consumeSourceRecord);
        sourceTask.configure(hConfig.subset(true, SOURCE_CONF_PREFIX, sourceName).asProperties());
        return sourceTask;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    private List<String> parseSourceList(String sourcesString) {
        return Arrays.stream(sourcesString.split(","))
                .map(String::strip)
                .filter(s -> !s.isBlank())
                .toList();
    }

    private void sleepMs(long ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {
            throw new JHarmonizerException("Interrupted while sleeping...", e);
        }
    }

    private void logProperties(Properties props) {
        logger.debug("Configuring Harmonizer with properties");
        props.keySet()
                .stream()
                .sorted()
                .forEach(k -> logger.debug("{}={}", k, props.get(k)));

    }

    public static final class Builder implements Harmonizer.Builder {
        private final List<HarmonizationListener<?, ?>> listeners = new ArrayList<>();
        private Properties props;

        private Builder() {

        }


        public Builder using(Properties props) {
            this.props = props;
            return this;
        }

        public Builder notifying(HarmonizationListener<?, ?> listener) {
            listeners.add(listener);
            return this;
        }

        public Harmonizer build() {
            var harmonizer = new DefaultHarmonizer(listeners.toArray(new HarmonizationListener[0]));
            harmonizer.configure(props);
            return harmonizer;
        }
    }

    public static final class BuilderFactory implements Harmonizer.BuilderFactory {

        @Override
        public Builder newBuilder() {
            return DefaultHarmonizer.newBuilder();
        }
    }
}
























