package io.javaside.jharmonizer;

import io.javaside.jharmonizer.common.Configurable;

import java.util.Properties;

public interface Filter<T> extends Configurable {

    boolean filter(T value);

    @Override
    default void configure(Properties props) {
        // no-op
    }
}
