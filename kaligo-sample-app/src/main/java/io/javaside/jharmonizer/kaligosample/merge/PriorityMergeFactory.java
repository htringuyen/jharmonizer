package io.javaside.jharmonizer.kaligosample.merge;

import io.javaside.jharmonizer.Merge;
import io.javaside.jharmonizer.MergeFactory;
import io.javaside.jharmonizer.kaligosample.model.Kaligo;

public class PriorityMergeFactory implements MergeFactory<String, Kaligo.Hotel> {

    @Override
    public Merge<String, Kaligo.Hotel> create() {
        return new PriorityMerge();
    }


}
