package io.javaside.jharmonizer.internal;

import io.javaside.jharmonizer.Merge;
import io.javaside.jharmonizer.MergingStrategy;

import java.util.Properties;

public class RelaxedMergingStrategy implements MergingStrategy<Object, Object> {

    @Override
    public boolean evaluate(Merge<Object, Object> merge) {
        var record = merge.flush();
        if (record != null) return true;
        throw new IllegalStateException("Merge should flush non null record when evaluated by MergingStrategy");
    }

    @Override
    public void configure(Properties props) {

    }
}
