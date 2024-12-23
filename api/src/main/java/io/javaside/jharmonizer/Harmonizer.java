package io.javaside.jharmonizer;

import io.javaside.jharmonizer.common.Configurable;

import java.io.Closeable;
import java.util.Properties;
import java.util.ServiceLoader;

public interface Harmonizer extends Configurable, Runnable, Closeable {

    interface Builder {

        Harmonizer build();

        Builder notifying(HarmonizationListener<?, ?> listener);

        Builder using(Properties props);

    }

    interface BuilderFactory {
        Builder newBuilder();
    }

    static Builder create() {
        return determineBuilderFactory().newBuilder();
    }

    static Builder create(String factoryClass) {
        return determineBuilderFactory(factoryClass).newBuilder();
    }

    private static BuilderFactory determineBuilderFactory() {
        return determineBuilderFactory("io.javaside.jharmonizer.internal.DefaultHarmonizerBuilderFactory");
    }

    private static BuilderFactory determineBuilderFactory(String factoryClass) {
        if (factoryClass == null || factoryClass.isBlank()) {
            return determineBuilderFactory();
        }

        final var loader = ServiceLoader.load(BuilderFactory.class);
        final var iterator = loader.iterator();
        if (!iterator.hasNext()) {
            throw new JHarmonizerException("No implementation of Harmonizer builder factory was found");
        }

        while (iterator.hasNext()) {
            var factory = iterator.next();
            if (factory.getClass().getName().equalsIgnoreCase(factoryClass)) {
                return factory;
            }
        }
        throw new JHarmonizerException(String.format("No Harmonizer builder factory %s was found", factoryClass));
    }
}
























