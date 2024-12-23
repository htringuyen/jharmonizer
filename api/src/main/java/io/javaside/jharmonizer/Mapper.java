package io.javaside.jharmonizer;

import io.javaside.jharmonizer.common.Configurable;

import java.util.Properties;

public interface Mapper<T, K, V> extends Configurable {

    MapResult<K, V> resultFrom(T value);

    @Override
    default void configure(Properties props) {
        // do nothing
    }

}
