package io.javaside.jharmonizer.kaligosample.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javaside.jharmonizer.HarmonizedRecord;
import io.javaside.jharmonizer.common.Configuration;
import io.javaside.jharmonizer.common.StaticDependency;
import io.javaside.jharmonizer.kaligosample.model.Kaligo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class KaligoSampleApplication {

    private static final Logger logger = LoggerFactory.getLogger(KaligoSampleApplication.class);

    private static final String HOTEL_INCLUDE_LIST = "source.{}.filter.hotel.include.list";

    private static final String DESTINATION_INCLUDE_LIST = "source.{}.filter.destination.include.list";

    private static final List<String> SOURCE_LIST = List.of("acme", "patagonia", "paperflies");

    private static final String UNDEFINED = "none";

    private static final String CONFIGURATION_PATH = "kaligo-sample-app.properties";

    private final ObjectMapper jsonMapper = StaticDependency.OBJECT_MAPPER;

    private final List<Kaligo.Hotel> harmonizedHotels = new LinkedList<>();

    private final boolean shouldPrintJson;


    private KaligoSampleApplication(boolean shouldPrintJson) {
        this.shouldPrintJson = shouldPrintJson;
    }


    public static KaligoSampleApplication create(boolean shouldPrintJson) {
        return new KaligoSampleApplication(shouldPrintJson);
    }


    public void runWith(String... args) {

        logger.info("Kaligo Sample Application started...");

        if (args.length != 2) {
            logger.error("Invalid number of arguments. Expected 2, found {}", args.length);
            throw new IllegalArgumentException("Invalid number of arguments");
        }

        logSetArg(args[0], "Selected hotels");
        logSetArg(args[1], "Selected destinations");

        var props = loadConfiguration().asProperties();
        overrideFilterProps(props, args[0], args[1]);

        try (var runner = HarmonizerRunner.createUsing(props,
                this::consumeHarmonizedRecord)) {
            runner.start();
            runner.awaitCompletion();
        }
        catch (IOException e) {
            throw new RuntimeException("Error closing runner", e);
        }

        if (shouldPrintJson) {
            printBeautifulJson();
        }

        logger.info("Kaligo Sample Application completed.");
    }

    // for testing
    List<Kaligo.Hotel> getHarmonizedHotels() {
        return Collections.unmodifiableList(harmonizedHotels);
    }

    private void overrideFilterProps(Properties props, String includedHotels, String includedDestinations) {
        SOURCE_LIST.forEach(source -> {
            props.setProperty(HOTEL_INCLUDE_LIST.replace("{}", source),
                    includedHotels.equalsIgnoreCase(UNDEFINED) ? "" : includedHotels);
            props.setProperty(DESTINATION_INCLUDE_LIST.replace("{}", source),
                    includedDestinations.equalsIgnoreCase(UNDEFINED) ? "" : includedDestinations);
        });
    }

    private void consumeHarmonizedRecord(HarmonizedRecord<String, Kaligo.Hotel> record) {
        harmonizedHotels.add(record.getValue());
    }

    private void printBeautifulJson() {
        try {
            printToConsole("Harmonized hotel objects:");
            var json = jsonMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(harmonizedHotels);
            printToConsole(json);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("Error writing JSON", e);
        }
    }

    private void printToConsole(String s) {
        System.out.println(s);
    }


    private void logSetArg(String arg, String header) {
        var set = Set.of(arg.split(","));
        logger.info("{}: {}", header, set);
    }

    private Configuration loadConfiguration() {
        try {
            return Configuration.load(CONFIGURATION_PATH, KaligoSampleApplication.class);
        }
        catch (IOException e) {
            throw new RuntimeException("Error loading configuration", e);
        }
    }
}



























