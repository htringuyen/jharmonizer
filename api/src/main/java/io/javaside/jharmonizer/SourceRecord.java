package io.javaside.jharmonizer;

import java.util.Objects;

public class SourceRecord<K, V> {

    private final K key;

    private final V value;

    private final String sourceId;

    public SourceRecord(K key, V value, String sourceId) {
        this.key = Objects.requireNonNull(key);
        this.value = Objects.requireNonNull(value);
        this.sourceId = Objects.requireNonNull(sourceId);
    }

    public String getSourceId() {
        return sourceId;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public static <K, V> SourceRecord<K, V> of(K key, V value, String sourceId) {
        return new SourceRecord<>(key, value, sourceId);
    }
}
