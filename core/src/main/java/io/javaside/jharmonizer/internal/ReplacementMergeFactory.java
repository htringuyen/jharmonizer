package io.javaside.jharmonizer.internal;

import io.javaside.jharmonizer.Merge;
import io.javaside.jharmonizer.MergeFactory;

import java.util.Properties;

public class ReplacementMergeFactory<K, V> implements MergeFactory<K, V> {

    @Override
    public Merge<K, V> create() {
        return new ReplacementMerge<>();
    }

    @Override
    public void configure(Properties props) {

    }
}
