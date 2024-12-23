package io.javaside.jharmonizer;

import io.javaside.jharmonizer.common.Configurable;

import java.util.Properties;

@FunctionalInterface
public interface HarmonizationListener<K, V> extends Configurable {

    void notify(HarmonizedRecord<K, V> harmonizedRecord);

    @Override
    default void configure(Properties props) {
        // no-op
    }
}
