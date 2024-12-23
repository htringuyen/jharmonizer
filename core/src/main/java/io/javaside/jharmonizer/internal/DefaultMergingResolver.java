package io.javaside.jharmonizer.internal;

import io.javaside.jharmonizer.*;
import io.javaside.jharmonizer.common.ConfigDef;
import io.javaside.jharmonizer.common.Configuration;

import java.util.*;

import static io.javaside.jharmonizer.common.MiscUtils.loadObject;
import static io.javaside.jharmonizer.common.MiscUtils.stringValueFrom;

public class DefaultMergingResolver implements MergingResolver {

    public static final ConfigDef MERGING_STRATEGY_CLASS_DEF = ConfigDef.of("strategy.class", true);

    public static final ConfigDef MERGING_STRATEGY_NAME_DEF = ConfigDef.of("strategy.name", true);

    public static final ConfigDef MERGE_FACTORY_CLASS_DEF = ConfigDef.of("merge.factory.class", true);

    public static final ConfigDef MERGE_FACTORY_NAME_DEF = ConfigDef.of("merge.factory.name", true);

    public static final String MERGING_STRATEGY_CONF_PREFIX = "strategy";

    public static final String MERGE_FACTORY_CONF_PREFIX = "merge.factory";

    private final Map<Object, Merge<?, ?>> mergeMap = Collections.synchronizedMap(new HashMap<>());

    private MergeFactory<?, ?> mergeFactory;

    private MergingStrategy<?, ?> strategy;

    private Set<HarmonizationListener<?, ?>> listeners = new HashSet<>();

    @Override
    public void configure(Properties props) {

        var strategyClass = stringValueFrom(props, MERGING_STRATEGY_CLASS_DEF);
        var strategyName = stringValueFrom(props, MERGING_STRATEGY_NAME_DEF);
        var strategy = loadObject(strategyClass, MergingStrategy.class);
        strategy.configure(Configuration.from(props)
                .subset(true, MERGING_STRATEGY_CONF_PREFIX, strategyName)
                .asProperties());
        this.strategy = strategy;

        var mergeFactoryClass = stringValueFrom(props, MERGE_FACTORY_CLASS_DEF);
        var mergeFactoryName = stringValueFrom(props, MERGE_FACTORY_NAME_DEF);
        var mergeFactory = loadObject(mergeFactoryClass, MergeFactory.class);
        mergeFactory.configure(Configuration.from(props)
                .subset(true, MERGE_FACTORY_CONF_PREFIX, mergeFactoryName)
                .asProperties());
        this.mergeFactory = mergeFactory;
    }

    @Override
    public void accept(SourceRecord<?, ?> record) {
        var mergeKey = mergeKeyFrom(record);
        var merge = (Merge<?, ?>) getOrCreateMerge(mergeKey);
        merge.combine(castRecord(record));
        if (strategy.evaluate(castMerge(merge))) {
            merge.markCompleted();
            removeMerge(mergeKey);
            notifyListeners(merge.flush());
        }
    }

    @Override
    public void addListener(HarmonizationListener<?, ?> listener) {
        listeners.add(listener);
    }

    @Override
    public boolean tryPushAllMerges() {

        if (mergeMap.isEmpty()) return true;

        var removedKeys = new LinkedList<>();
        mergeMap.forEach((key, merge) -> {
            if (strategy.evaluate(castMerge(merge))) {
                merge.markCompleted();
                removedKeys.add(key);
                notifyListeners(merge.flush());
            }
        });
        removedKeys.forEach(this::removeMerge);
        return mergeMap.isEmpty();
    }

    @Override
    public int getMergeCount() {
        return mergeMap.size();
    }

    private void notifyListeners(HarmonizedRecord<?, ?> record) {
        listeners.forEach(l -> l.notify(castHarmonizedRecord(record)));
    }

    private Merge<?, ?> getOrCreateMerge(Object key) {
        if (mergeMap.containsKey(key)) {
            return mergeMap.get(key);
        }
        var merge = mergeFactory.create();
        mergeMap.put(key, merge);
        return merge;
    }

    private void removeMerge(Object key) {
        mergeMap.remove(key);
    }

    private Object mergeKeyFrom(SourceRecord<?, ?> record) {
        return record.getKey();
    }

    private <K, V> SourceRecord<K, V> castRecord(SourceRecord<?, ?> record) {
        return (SourceRecord<K, V>) record;
    }

    private <K, V> Merge<K, V> castMerge(Merge<?, ?> merge) {
        return (Merge<K, V>) merge;
    }

    private <K, V> HarmonizedRecord<K, V> castHarmonizedRecord(HarmonizedRecord<?, ?> record) {
        return (HarmonizedRecord<K, V>) record;
    }
}



























