package io.javaside.jharmonizer;

import io.javaside.jharmonizer.common.Configurable;

public interface MergingResolver extends Configurable {

    void accept(SourceRecord<?, ?> record);

    void addListener(HarmonizationListener<?, ?> listener);

    boolean tryPushAllMerges();

    int getMergeCount();
}
