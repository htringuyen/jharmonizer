package io.javaside.jharmonizer.internal;

import io.javaside.jharmonizer.AbstractMerge;
import io.javaside.jharmonizer.HarmonizedRecord;
import io.javaside.jharmonizer.SourceRecord;

public class ReplacementMerge<K, V> extends AbstractMerge<K, V> {

    private SourceRecord<K, V> record;

    @Override
    public void doCombine(SourceRecord<K, V> record) {
        this.record = record;
    }

    @Override
    public HarmonizedRecord<K, V> flush() {
        return HarmonizedRecord.from(record);
    }
}
