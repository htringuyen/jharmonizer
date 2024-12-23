package io.javaside.jharmonizer.mockdomain;

import io.javaside.jharmonizer.Merge;
import io.javaside.jharmonizer.MergeFactory;

public class FlatUserMergeFactory implements MergeFactory<Long, FlatUser> {

    @Override
    public Merge<Long, FlatUser> create() {
        return new FlatUserMerge();
    }
}
