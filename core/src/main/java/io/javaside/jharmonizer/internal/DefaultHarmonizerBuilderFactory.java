package io.javaside.jharmonizer.internal;

import io.javaside.jharmonizer.Harmonizer;

public class DefaultHarmonizerBuilderFactory implements Harmonizer.BuilderFactory {

    @Override
    public Harmonizer.Builder newBuilder() {
        return DefaultHarmonizer.newBuilder();
    }

}
