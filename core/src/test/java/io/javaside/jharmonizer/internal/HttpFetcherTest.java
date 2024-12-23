package io.javaside.jharmonizer.internal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpFetcherTest {

    private static final Logger logger = LoggerFactory.getLogger(HttpFetcherTest.class);

    private static final String URL = "https://jsonplaceholder.typicode.com/users";

    private static final String SOURCE_ID = "jsonplaceholder/users";

    private HttpFetcher httpFetcher;

    private DefaultMergingResolver mergingResolver;

    @BeforeEach
    void setup() {
        var props = new Properties();
        props.put(HttpFetcher.HTTP_URL_DEF.getName(), URL);
        props.put(HttpFetcher.SOURCE_ID_DEF.getName(), SOURCE_ID);
        httpFetcher = new HttpFetcher();
        httpFetcher.configure(props);
    }

    @AfterEach
    void teardown() {
        httpFetcher.close();
    }

    @Test
    void fetchACMERecords() {
        var result = httpFetcher.fetch();
        result.forEach(r -> logger.info(r.getPayload().toString()));
        assertEquals(10, result.size());
    }
}






























