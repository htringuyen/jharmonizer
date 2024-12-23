package io.javaside.jharmonizer.internal;

import io.javaside.jharmonizer.Harmonizer;
import io.javaside.jharmonizer.JHarmonizerException;
import io.javaside.jharmonizer.common.Configuration;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HarmonizerLoadTest {

    private static final String HARMONIZER_PROPERTIES = "harmonizer-1.properties";

    @Test
    void loadDefaultHarmonizer() throws IOException {
        var props = Configuration.load(HARMONIZER_PROPERTIES, HarmonizerLoadTest.class)
                .asProperties();
        try (var harmonizer = Harmonizer.create()
                .using(props)
                .build()) {
            assertNotNull(harmonizer);
        }
        catch (IOException e) {
            throw new JHarmonizerException(e);
        }
    }
}
