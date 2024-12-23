package io.javaside.jharmonizer.internal;

import io.javaside.jharmonizer.HarmonizationListener;
import io.javaside.jharmonizer.HarmonizedRecord;
import io.javaside.jharmonizer.mockdomain.FlatUser;

import java.util.LinkedList;
import java.util.List;

public class ListingListener<K, V> implements HarmonizationListener<K, V> {

    private final List<HarmonizedRecord<K, V>> harmonizedRecords = new LinkedList<>();

    @Override
    public void notify(HarmonizedRecord<K, V> harmonizedRecord) {
        harmonizedRecords.add(harmonizedRecord);
    }

    public List<HarmonizedRecord<K, V>> records() {
        return harmonizedRecords;
    }
}
