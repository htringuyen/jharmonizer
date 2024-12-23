package io.javaside.jharmonizer.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javaside.jharmonizer.Converter;
import io.javaside.jharmonizer.FetcherRecord;
import io.javaside.jharmonizer.JHarmonizerException;
import io.javaside.jharmonizer.StructRecord;
import io.javaside.jharmonizer.common.ConfigDef;
import io.javaside.jharmonizer.common.StaticDependency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static io.javaside.jharmonizer.common.MiscUtils.stringValueFrom;

public class JsonConverter implements Converter {

    private static final Logger logger = LoggerFactory.getLogger(JsonConverter.class);

    public static final ConfigDef TARGET_TYPE_DEF = ConfigDef.of("target.type", true);

    private final ObjectMapper jsonMapper = StaticDependency.OBJECT_MAPPER;

    private Class<?> targetType;

    @Override
    public void configure(Properties props) {
        var targetClassName = stringValueFrom(props, TARGET_TYPE_DEF);
        try {
            targetType = Class.forName(targetClassName);
        } catch (ClassNotFoundException e) {
            throw new JHarmonizerException(String.format("Not found target type %s", targetClassName), e);
        }
    }

    @Override
    public StructRecord convert(FetcherRecord fetcherRecord) {
        try {
            //logger.debug("Converting fetcher record: {}", fetcherRecord.getPayload());
            //var structValue = jsonMapper.readValue(fetcherRecord.getPayload().toString(), User.class);
            //logger.debug("Read struct value of type: {}", targetType);
            var result = StructRecord.of(jsonMapper.readValue(fetcherRecord.getPayload().toString(), targetType),
                    fetcherRecord.getSourceId());
            //var result = StructRecord.of(structValue, fetcherRecord.getSourceId());
            //logger.debug("Converted struct record: {}", result.getPayload());
            return result;
        } catch (JsonProcessingException e) {
            throw new JHarmonizerException("Json conversion error", e);
        }
    }
}
