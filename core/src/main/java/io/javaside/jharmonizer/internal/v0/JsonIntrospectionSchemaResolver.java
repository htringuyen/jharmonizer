package io.javaside.jharmonizer.internal.v0;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javaside.jharmonizer.FetcherRecord;
import io.javaside.jharmonizer.common.StaticDependency;
import io.javaside.jharmonizer.v0.SchemaRecord;
import io.javaside.jharmonizer.v0.SchemaResolver;
import org.apache.avro.Schema;

import java.util.Properties;

public class JsonIntrospectionSchemaResolver implements SchemaResolver {

    private final ObjectMapper jsonMapper = StaticDependency.OBJECT_MAPPER;

    @Override
    public SchemaRecord resolve(FetcherRecord record) {
        throw new RuntimeException();
    }

    @Override
    public void configure(Properties props) {

    }

    private Schema introspectSchemaFromObject(JsonNode jsonObj, String recordName) {
        throw new RuntimeException();
    }

    private Schema introspectSchemaFromField(JsonNode fieldObj, String fieldName) {
        throw new RuntimeException();
    }
}
