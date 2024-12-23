package io.javaside.jharmonizer;

import io.javaside.jharmonizer.common.Configurable;

import java.util.Properties;

public interface MergingStrategy<K, V> extends Configurable {

    boolean evaluate(Merge<K, V> merge);

}
