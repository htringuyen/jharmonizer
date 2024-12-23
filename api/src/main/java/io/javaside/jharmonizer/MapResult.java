package io.javaside.jharmonizer;

public class MapResult<K,V> {

    private final K key;

    private final V value;

    private MapResult(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public static <K,V> MapResult<K,V> of(K key, V value) {
        return new MapResult<>(key, value);
    }
}
