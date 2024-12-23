package io.javaside.jharmonizer.kaligosample.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javaside.jharmonizer.HarmonizationListener;
import io.javaside.jharmonizer.HarmonizedRecord;
import io.javaside.jharmonizer.Harmonizer;
import io.javaside.jharmonizer.JHarmonizerException;
import io.javaside.jharmonizer.common.StaticDependency;
import io.javaside.jharmonizer.kaligosample.model.Kaligo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.*;

public class HarmonizerRunner<K, V> implements Closeable {

    private static final Logger logger = LoggerFactory.getLogger(HarmonizerRunner.class);

    private final Harmonizer harmonizer;

    private Future<?> harmonizerFuture;

    private ExecutorService executor;

    private HarmonizerRunner(Properties props, HarmonizationListener<K, V> listener) {
        harmonizer = Harmonizer.create()
                .using(props)
                .notifying(listener)
                .build();
    }

    public void start() {
        executor = Executors.newSingleThreadExecutor();
        try {
            harmonizerFuture = executor.submit(harmonizer);
        }
        catch (JHarmonizerException e) {
            logger.error("Error running harmonizer", e);
        }
    }

    public void awaitCompletion() {
        try {
            harmonizerFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Error waiting for harmonizer to complete", e);
        }
    }

    @Override
    public void close() throws IOException {
        harmonizer.close();
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                throw new RuntimeException("Runner executor termination timeout");
            }
        }
        catch (InterruptedException e) {
            throw new RuntimeException("Error waiting for runner executor termination", e);
        }
    }

    public static <K, V> HarmonizerRunner<K, V> createUsing(Properties props, HarmonizationListener<K, V> listener) {
        return new HarmonizerRunner<>(props, listener);
    }

    private void printString(String str) {
        System.out.println(str);
    }
}



































