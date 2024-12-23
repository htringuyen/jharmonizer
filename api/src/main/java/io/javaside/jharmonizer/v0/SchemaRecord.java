package io.javaside.jharmonizer.v0;

import io.javaside.jharmonizer.FetcherRecord;

public class SchemaRecord extends FetcherRecord {

    private final SchemaDescriptor keyDescriptor;

    private final SchemaDescriptor valueDescriptor;

    public SchemaRecord(String sourceId, Object payload, SchemaDescriptor keyDescriptor, SchemaDescriptor valueDescriptor) {
        super(sourceId, payload);
        this.keyDescriptor = keyDescriptor;
        this.valueDescriptor = valueDescriptor;
    }

    public SchemaDescriptor getKeyDescriptor() {
        return keyDescriptor;
    }

    public SchemaDescriptor getValueDescriptor() {
        return valueDescriptor;
    }

    public SchemaRecord from(FetcherRecord record, SchemaDescriptor keyDescriptor, SchemaDescriptor valueDescriptor) {
        return new SchemaRecord(
                record.getSourceId(),
                record.getPayload(),
                keyDescriptor,
                valueDescriptor);
    }
}
