package io.javaside.jharmonizer.internal;

import io.javaside.jharmonizer.Merge;
import io.javaside.jharmonizer.MergingStrategy;
import io.javaside.jharmonizer.common.ConfigDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static io.javaside.jharmonizer.common.MiscUtils.stringValueFrom;

public class TimeLimitedGreedyStrategy implements MergingStrategy<Object, Object> {

    private static final Logger logger = LoggerFactory.getLogger(TimeLimitedGreedyStrategy.class);

    public static final ConfigDef LIMIT_TIME_MS_DEF = ConfigDef.of("limit.time.ms", String.valueOf(Long.MAX_VALUE));

    public static final ConfigDef LIMIT_EVALUATION_COUNT_DEF = ConfigDef.of("limit.evaluation.count", String.valueOf(Long.MAX_VALUE));

    private static final ConfigDef DESIRED_SOURCES_DEF = ConfigDef.of("source.desired.list", true);

    private static final String START_EVALUATION_TIME_MD_NAME = "_start.evaluation.time";

    private static final String EVALUATION_COUNT_MD_NAME = "_evaluation.period.count";

    private long limitTimeMs;

    private long limitEvaluationCount;

    private Set<String> desiredSources;

    @Override
    public void configure(Properties props) {
        limitTimeMs = Long.parseLong(stringValueFrom(props, LIMIT_TIME_MS_DEF));
        limitEvaluationCount = Long.parseLong(stringValueFrom(props, LIMIT_EVALUATION_COUNT_DEF));
        desiredSources = Arrays.stream(stringValueFrom(props, DESIRED_SOURCES_DEF).split(","))
                .map(String::strip)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public boolean evaluate(Merge<Object, Object> merge) {
        if (limitTimeMs < Long.MAX_VALUE) {
            var startTime = (Long) merge.getMetadata(START_EVALUATION_TIME_MD_NAME);
            if (startTime == null) {
                merge.putMetadata(START_EVALUATION_TIME_MD_NAME, milliNow());
            }
            else if (milliNow() - startTime > limitTimeMs) {
                //logger.debug("Merge time limit reach evaluate to true");
                return true;
            }
        }

        if (limitEvaluationCount < Long.MAX_VALUE) {
            var evaluationCount = (Long) merge.getMetadata(EVALUATION_COUNT_MD_NAME);
            if (evaluationCount == null) {
                merge.putMetadata(EVALUATION_COUNT_MD_NAME, 1L);
                evaluationCount = 1L;
            }
            if (evaluationCount > limitEvaluationCount) {
                return true;
            }
            merge.putMetadata(EVALUATION_COUNT_MD_NAME, evaluationCount + 1);
        }
        return isDesiredSourcesMatch(merge.getMergedSources());
    }

    private long milliNow() {
        return Instant.now().toEpochMilli();
    }

    private boolean isDesiredSourcesMatch(Set<String> sources) {
        return sources.containsAll(desiredSources);
    }
}



















