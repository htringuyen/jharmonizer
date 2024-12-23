package io.javaside.jharmonizer.mockdomain;

import io.javaside.jharmonizer.Filter;
import io.javaside.jharmonizer.common.ConfigDef;
import io.javaside.jharmonizer.common.MiscUtils;

import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class WorkIdFilter implements Filter<WorkUser> {

    public static final ConfigDef ID_EXCLUDE_LIST = ConfigDef.of("id.exclude.list", true);

    private Set<Long> excludedIds;

    @Override
    public void configure(Properties props) {
        excludedIds = Arrays.stream(MiscUtils.stringValueFrom(props, ID_EXCLUDE_LIST).split(","))
                .map(String::strip)
                .filter(s -> !s.isBlank())
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean filter(WorkUser user) {
        return !excludedIds.contains(user.id());
    }

}




































