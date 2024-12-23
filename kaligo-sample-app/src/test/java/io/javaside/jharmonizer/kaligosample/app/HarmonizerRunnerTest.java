package io.javaside.jharmonizer.kaligosample.app;

import io.javaside.jharmonizer.common.Configuration;
import io.javaside.jharmonizer.internal.ListingListener;
import io.javaside.jharmonizer.kaligosample.model.Kaligo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HarmonizerRunnerTest {

    private static final Logger logger = LoggerFactory.getLogger(HarmonizerRunnerTest.class);

    private static final String CONFIGURATION_PATH = "kaligo-sample-app.properties";

    private Properties props;

    @BeforeEach
    void setUp() throws Exception{
        var conf = Configuration.load(CONFIGURATION_PATH, HarmonizerRunnerTest.class);
        this.props = conf.asProperties();
    }

    @Test
    void runHarmonizerRunner() {
        var listener = new ListingListener<String, Kaligo.Hotel>();

        try (var runner = HarmonizerRunner.createUsing(props, listener)) {
            runner.start();
            runner.awaitCompletion();
        }
        catch (IOException e) {
            logger.error("Error", e);
        }

        var records = listener.records();
        assertEquals(3, records.size());
        records.forEach(r -> logger.info(r.getValue().toString()));
    }
}























