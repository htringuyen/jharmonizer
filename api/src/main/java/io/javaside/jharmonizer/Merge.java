package io.javaside.jharmonizer;

import java.util.List;
import java.util.Set;

public interface Merge<K, V> {

    HarmonizedRecord<K, V> flush();

    void combine(SourceRecord<K, V> record);

    void markCompleted();

    Set<String> getMergedSources();

    boolean isCompleted();

    void putMetadata(String name, Object value);

    Object getMetadata(String name);

    boolean hasMetadata(String name);
}
