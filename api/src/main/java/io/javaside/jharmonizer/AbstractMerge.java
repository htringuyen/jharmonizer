package io.javaside.jharmonizer;

import java.util.*;

public abstract class AbstractMerge<K, V> implements Merge<K, V> {

    private boolean completed = false;

    private final Map<String, Object> metadata = new HashMap<>();

    private final Set<String> mergedSources = new TreeSet<>();

    @Override
    public void markCompleted() {
        completed = true;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    @Override
    public void putMetadata(String name, Object value) {
        metadata.put(name, value);
    }

    @Override
    public Object getMetadata(String name) {
        return metadata.get(name);
    }

    @Override
    public Set<String> getMergedSources() {
        return Collections.unmodifiableSet(mergedSources);
    }

    protected abstract void doCombine(SourceRecord<K, V> record);

    @Override
    public final void combine(SourceRecord<K, V> record) {
        mergedSources.add(record.getSourceId());
        doCombine(record);
    }

    @Override
    public boolean hasMetadata(String name) {
        return metadata.containsKey(name);
    }
}










































