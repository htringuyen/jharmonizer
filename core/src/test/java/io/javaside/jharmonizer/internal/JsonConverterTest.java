package io.javaside.jharmonizer.internal;

import io.javaside.jharmonizer.Converter;
import io.javaside.jharmonizer.FetcherRecord;
import io.javaside.jharmonizer.mockdomain.WorkUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JsonConverterTest {

    private static final Logger logger = LoggerFactory.getLogger(JsonConverterTest.class);

    private static final String URL = "https://jsonplaceholder.typicode.com/users";

    private static final String SOURCE_ID = "jsonplaceholder/users";

    private HttpFetcher fetcher;

    private List<FetcherRecord> records;

    private Converter converter;

    @BeforeEach
    void setup() {
        var props = new Properties();
        props.put(HttpFetcher.HTTP_URL_DEF.getName(), URL);
        props.put(HttpFetcher.SOURCE_ID_DEF.getName(), SOURCE_ID);
        fetcher = new HttpFetcher();
        fetcher.configure(props);
        records = fetcher.fetch();

        var converterProps = new Properties();
        converterProps.put(JsonConverter.TARGET_TYPE_DEF.getName(), WorkUser.class.getName());
        converter = new JsonConverter();
        converter.configure(converterProps);
    }

    @AfterEach
    void teardown() {
        fetcher.close();
    }

    @Test
    void convert() {
        var json = records.getFirst().getPayload().toString();

        var structRecord = converter.convert(records.getFirst());

        assertNotNull(structRecord);
    }

}




















