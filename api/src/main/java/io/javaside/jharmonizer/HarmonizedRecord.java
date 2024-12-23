package io.javaside.jharmonizer;

import java.util.Objects;

public class HarmonizedRecord<K, V> {

    private final K key;

    private final V value;

    private HarmonizedRecord(K key, V value) {
        /*this.key = Objects.requireNonNull(key);
        this.value = Objects.requireNonNull(value);*/
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public static <K, V> HarmonizedRecord<K, V> of(K key, V value) {
        return new HarmonizedRecord<>(key, value);
    }

    public static <K, V> HarmonizedRecord<K, V> from(SourceRecord<K, V> record) {
        return new HarmonizedRecord<>(record.getKey(), record.getValue());
    }
}
