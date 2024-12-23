package io.javaside.jharmonizer;

public class StructRecord {

    private final String sourceId;

    private final Object payload;

    public StructRecord(String sourceId, Object payload) {
        this.sourceId = sourceId;
        this.payload = payload;
    }

    public String getSourceId() {
        return sourceId;
    }

    public Object getPayload() {
        return payload;
    }

    public static StructRecord of(Object payload, String sourceId) {
        return new StructRecord(sourceId, payload);
    }
}
