package io.javaside.jharmonizer.internal;

import io.javaside.jharmonizer.Converter;
import io.javaside.jharmonizer.HarmonizedRecord;
import io.javaside.jharmonizer.SourceRecord;
import io.javaside.jharmonizer.StructRecord;
import io.javaside.jharmonizer.mockdomain.FlatUser;
import io.javaside.jharmonizer.mockdomain.User;
import io.javaside.jharmonizer.mockdomain.UserFlatteningMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DefaultMergingResolverTest {

    private static final Logger logger = LoggerFactory.getLogger(DefaultMergingResolverTest.class);

    private static final String URL = "https://jsonplaceholder.typicode.com/users";

    private static final String SOURCE_ID = "jsonplaceholder/users";

    private HttpFetcher fetcher;

    private DefaultMergingResolver resolver;

    private List<SourceRecord<Long, FlatUser>> sourceRecords;

    private UserFlatteningMapper flatteningMapper;

    private ListingListener<Long, FlatUser> listener;

    @BeforeEach
    void setUp() {
        this.fetcher = createFetcher();
        this.resolver = createMergingResolver();
        this.flatteningMapper = new UserFlatteningMapper();
        this.listener = new ListingListener<>();
        var jsonConverter = createConverter();
        sourceRecords = fetcher.fetch()
                .stream()
                .map(jsonConverter::convert)
                .map(this::structRecordToMergingRecord)
                .toList();
        resolver.addListener(listener);
    }

    @AfterEach
    void tearDown() {
        this.fetcher.close();
    }

    private HttpFetcher createFetcher() {
        var props = new Properties();
        props.put(HttpFetcher.HTTP_URL_DEF.getName(), URL);
        props.put(HttpFetcher.SOURCE_ID_DEF.getName(), SOURCE_ID);
        var httpFetcher = new HttpFetcher();
        httpFetcher.configure(props);
        return httpFetcher;
    }

    private Converter createConverter() {
        var props = new Properties();
        props.put(JsonConverter.TARGET_TYPE_DEF.getName(), User.class.getName());
        var converter = new JsonConverter();
        converter.configure(props);
        return converter;
    }

    private DefaultMergingResolver createMergingResolver() {
        var props = new Properties();
        props.put(DefaultMergingResolver.MERGING_STRATEGY_CLASS_DEF.getName(), RelaxedMergingStrategy.class.getName());
        props.put(DefaultMergingResolver.MERGING_STRATEGY_NAME_DEF.getName(), "relaxed");
        props.put(DefaultMergingResolver.MERGE_FACTORY_CLASS_DEF.getName(), ReplacementMergeFactory.class.getName());
        props.put(DefaultMergingResolver.MERGE_FACTORY_NAME_DEF.getName(), "replacement");
        var resolver = new DefaultMergingResolver();
        resolver.configure(props);
        return resolver;
    }

    private SourceRecord<Long, FlatUser> structRecordToMergingRecord(StructRecord structRecord) {
        try {
            var mapResult = flatteningMapper.resultFrom((User) structRecord.getPayload());
            return SourceRecord.of(mapResult.getKey(), mapResult.getValue(), structRecord.getSourceId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void fetchAndMerge() {
        sourceRecords.forEach(resolver::accept);
        var records = listener.records();
        assertEquals(10, records.size());
        records.forEach(this::validateHarmonizedRecord);
    }


    private void validateHarmonizedRecord(HarmonizedRecord<Long, FlatUser> record) {
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


}




















