package io.javaside.jharmonizer.internal;

import io.javaside.jharmonizer.SourceRecord;

@FunctionalInterface
public interface SourceConsumer {
    void accept(SourceRecord<?, ?> record);
}
