package io.javaside.jharmonizer;

import io.javaside.jharmonizer.common.Configurable;

import java.util.Properties;

public interface MergeFactory<K, V> extends Configurable {

    Merge<K, V> create();

    @Override
    default void configure(Properties props) {
        // do nothing
    }
}
