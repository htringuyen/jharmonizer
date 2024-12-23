package io.javaside.jharmonizer.internal;

import io.javaside.jharmonizer.HarmonizedRecord;
import io.javaside.jharmonizer.Harmonizer;
import io.javaside.jharmonizer.common.Configuration;
import io.javaside.jharmonizer.mockdomain.FlatUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultHarmonizerTest {

    private static final Logger logger = LoggerFactory.getLogger(DefaultHarmonizerTest.class);

    private static final String HARMONIZER_PROPERTIES = "harmonizer-1.properties";

    private final List<Long> partialRecordIds = List.of(1L, 3L, 5L);

    private Harmonizer harmonizer;

    private ExecutorService executor;

    private ListingListener<Long, FlatUser> listener;

    @BeforeEach
    void setUp() throws Exception {
        this.executor = Executors.newCachedThreadPool();
        var props = Configuration.load(HARMONIZER_PROPERTIES, DefaultHarmonizerTest.class)
                .asProperties();
        this.listener = new ListingListener<Long, FlatUser>();
        this.harmonizer = DefaultHarmonizer.newBuilder()
                .using(props)
                .notifying(listener)
                .build();
    }

    //@AfterEach
    void tearDown() throws IOException, InterruptedException {
        logger.info("Shutting down harmonizer");
        harmonizer.close();
        this.executor.shutdown();
        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
            logger.info("Executor termination timed out");
        }
        logger.info("Test executor shutdown completely");
    }

    @Test
    void runHarmonizer() throws Exception {
        var future = executor.submit(harmonizer);
        future.get();

        var records = listener.records();
        assertEquals(10, records.size());
        for (var record : records) {
            assertNotNull(record.getValue());
            assertNotNull(record.getValue().id());
            assertNotNull(record.getKey());
            if (partialRecordIds.contains(record.getKey())) {
                validatePartialMergeRecord(record);
            }
            else {
                validateFullMergeRecord(record);
            }
        }
        tearDown();
    }

    private void sleepMs(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void validateFullMergeRecord(HarmonizedRecord<Long, FlatUser> record) {
        assertNotNull(record.getKey());
        assertNotNull(record.getValue());
        var user = record.getValue();
        assertNotNull(user.id());
        assertNotNull(user.name());
        assertNotNull(user.username());
        assertNotNull(user.email());
        assertNotNull(user.street());
        assertNotNull(user.suite());
        assertNotNull(user.city());
        assertNotNull(user.zipcode());
        assertNotNull(user.latitude());
        assertNotNull(user.longitude());
        assertNotNull(user.phone());
        assertNotNull(user.website());
        assertNotNull(user.companyName());
        assertNotNull(user.catchPhrase());
        assertNotNull(user.businessStrategy());
    }

    private void validatePartialMergeRecord(HarmonizedRecord<Long, FlatUser> record) {
        assertNotNull(record.getKey());
        assertNotNull(record.getValue());
        var user = record.getValue();
        assertNotNull(user.id());
        assertNotNull(user.name());
        assertNotNull(user.username());
        assertNotNull(user.email());
        assertNotNull(user.street());
        assertNotNull(user.suite());
        assertNotNull(user.city());
        assertNotNull(user.zipcode());
        assertNotNull(user.latitude());
        assertNotNull(user.longitude());
        assertNotNull(user.phone());
        assertNull(user.website());
        assertNull(user.companyName());
        assertNull(user.catchPhrase());
        assertNull(user.businessStrategy());
    }
}























