package io.javaside.jharmonizer.v0;

public interface SchemaDescriptor {

    enum SchemaSystem {
        JSON,
        AVRO,
        PROTOBUF
    }

    String getSchemaPayload();

    SchemaSystem getSchemaSystem();

    static SchemaDescriptor create(String payload, SchemaSystem schemaSystem) {
        return new SchemaDescriptor() {
            @Override
            public String getSchemaPayload() {
                return payload;
            }

            @Override
            public SchemaSystem getSchemaSystem() {
                return schemaSystem;
            }
        };
    }
}
