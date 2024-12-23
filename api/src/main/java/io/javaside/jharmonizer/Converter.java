package io.javaside.jharmonizer;

import io.javaside.jharmonizer.common.Configurable;

import java.util.Properties;

public interface Converter extends Configurable {

    StructRecord convert(FetcherRecord record);

    @Override
    default void configure(Properties props) {
        // do nothing
    }
}
