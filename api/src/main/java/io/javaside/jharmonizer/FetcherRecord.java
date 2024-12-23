package io.javaside.jharmonizer;

public class FetcherRecord {

    private final String sourceId;

    private final Object payload;

   public FetcherRecord(String sourceId, Object payload) {
        this.sourceId = sourceId;
        this.payload = payload;
    }

    public String getSourceId() {
        return sourceId;
    }

    public Object getPayload() {
        return payload;
    }

    public static FetcherRecord of(Object payload, String sourceId) {
        return new FetcherRecord(sourceId, payload);
    }
}
