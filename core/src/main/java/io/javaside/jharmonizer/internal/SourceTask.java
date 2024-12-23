package io.javaside.jharmonizer.internal;

import io.javaside.jharmonizer.*;
import io.javaside.jharmonizer.common.ConfigDef;
import io.javaside.jharmonizer.common.Configurable;
import io.javaside.jharmonizer.common.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static io.javaside.jharmonizer.common.MiscUtils.loadObject;
import static io.javaside.jharmonizer.common.MiscUtils.stringValueFrom;

public class SourceTask implements Configurable, Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SourceTask.class);

    public static final ConfigDef SOURCE_FETCHER_CLASS_DEF = ConfigDef.of("fetcher.class", true);

    public static final ConfigDef SOURCE_CONVERTER_CLASS_DEF = ConfigDef.of("converter.class", true);

    public static final ConfigDef SOURCE_FILTER_CLASS_DEF = ConfigDef.of("filter.class", "");

    public static final ConfigDef SOURCE_MAPPER_CLASS_DEF = ConfigDef.of("mapper.class", true);

    public static final String FETCHER_CONF_PREFIX = "fetcher";

    public static final String CONVERTER_CONF_PREFIX = "converter";

    public static final String FILTER_CONF_PREFIX = "filter";

    public static final String MAPPER_CONF_PREFIX = "mapper";

    private Fetcher fetcher;

    private Converter converter;

    private Filter<?> filter;

    private Mapper<?, ?, ?> mapper;

    private final SourceConsumer consumer;

    private final String sourceTaskName;

    public SourceTask(String sourceTaskName, SourceConsumer consumer) {
        this.sourceTaskName = sourceTaskName;
        this.consumer = consumer;
    }

    @Override
    public void configure(Properties props) {
        var config = Configuration.from(props);

        var fetcherClass = stringValueFrom(props, SOURCE_FETCHER_CLASS_DEF);
        fetcher = loadObject(fetcherClass, Fetcher.class);
        fetcher.configure(config.subset(FETCHER_CONF_PREFIX, true).asProperties());

        var converterClass = stringValueFrom(props, SOURCE_CONVERTER_CLASS_DEF);
        converter = loadObject(converterClass, Converter.class);
        converter.configure(config.subset(CONVERTER_CONF_PREFIX, true).asProperties());

        var filterClass = stringValueFrom(props, SOURCE_FILTER_CLASS_DEF);
        if (!filterClass.isBlank()) {
            filter = loadObject(filterClass, Filter.class);
            filter.configure(config.subset(FILTER_CONF_PREFIX, true).asProperties());
        }

        var mapperClass = stringValueFrom(props, SOURCE_MAPPER_CLASS_DEF);
        mapper = loadObject(mapperClass, Mapper.class);
        mapper.configure(config.subset(MAPPER_CONF_PREFIX, true).asProperties());
    }

    @Override
    public void run() {
        logger.debug("Source task {} started...", sourceTaskName);

        var fetcherRecord = fetcher.fetch();
        //logger.debug("Fetched {} records", fetcherRecord.size());

        var structRecords = fetcherRecord.stream().map(converter::convert).toList();
        //logger.debug("Converted {} records", structRecords.size());

        fetcherRecord.stream()
                .map(converter::convert)
                .filter(r -> filter == null || filter.filter(extractPayload(r)))
                .map(this::mapToSourceRecord)
                .forEach(consumer::accept);

        logger.debug("Source task {} stopped...", sourceTaskName);
    }

    private SourceRecord<?, ?> mapToSourceRecord(StructRecord record) {
        var mapResult = mapper.resultFrom(extractPayload(record));
        return SourceRecord.of(mapResult.getKey(), mapResult.getValue(), record.getSourceId());
    }

    private<T> T extractPayload(StructRecord record) {
        return (T) record.getPayload();
    }

    public static SourceTask newInstance(String name, SourceConsumer consumer) {
        return new SourceTask(name, consumer);
    }
}












