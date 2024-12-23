package io.javaside.jharmonizer.mockdomain;

import io.javaside.jharmonizer.HarmonizedRecord;
import io.javaside.jharmonizer.SourceRecord;
import io.javaside.jharmonizer.AbstractMerge;

import java.util.HashMap;
import java.util.Map;

public class FlatUserMerge extends AbstractMerge<Long, FlatUser> {

    private static final String WORK_SOURCE_ID = "worku-src";

    private static final String LIFE_SOURCE_ID = "lifeu-src";

    private final Map<String, Object> fieldMap = new HashMap<>();

    private HarmonizedRecord<Long, FlatUser> cacheRecord = null;

    @Override
    protected void doCombine(SourceRecord<Long, FlatUser> record) {
        var user = record.getValue();
        if (record.getSourceId().equals(WORK_SOURCE_ID)) {
            setField("id", user.id());
            setField("name", user.name());
            setField("username", user.username());
            setField("email", user.email());
            setField("phone", user.phone());
            setField("website", user.website());
            setField("companyName", user.companyName());
            setField("catchPhrase", user.catchPhrase());
            setField("businessStrategy", user.businessStrategy());
        }
        else if (record.getSourceId().equals(LIFE_SOURCE_ID)) {
            setField("id", user.id());
            setField("name", user.name());
            setField("username", user.username());
            setField("email", user.email());
            setField("phone", user.phone());
            setField("street", user.street());
            setField("suite", user.suite());
            setField("city", user.city());
            setField("zipcode", user.zipcode());
            setField("latitude", user.latitude());
            setField("longitude", user.longitude());
        }
    }

    private void setField(String name, Object value) {
        if (!fieldMap.containsKey(name)) {
            fieldMap.put(name, value);
        }
    }

    private<T> T getField(String name, Class<T> type) {
        return type.cast(fieldMap.get(name));
    }

    @Override
    public HarmonizedRecord<Long, FlatUser> flush() {

        if (cacheRecord != null) return cacheRecord;

        var user = FlatUser.builder()
                .id(getField("id", Long.class))
                .name(getField("name", String.class))
                .username(getField("username", String.class))
                .email(getField("email", String.class))
                .phone(getField("phone", String.class))
                .website(getField("website", String.class))
                .companyName(getField("companyName", String.class))
                .catchPhrase(getField("catchPhrase", String.class))
                .businessStrategy(getField("businessStrategy", String.class))
                .street(getField("street", String.class))
                .suite(getField("suite", String.class))
                .city(getField("city", String.class))
                .zipcode(getField("zipcode", String.class))
                .latitude(getField("latitude", Double.class))
                .longitude(getField("longitude", Double.class))
                .build();

        cacheRecord = HarmonizedRecord.of(user.id(), user);
        return cacheRecord;
    }
}


























