package io.javaside.jharmonizer.mockdomain;

import io.javaside.jharmonizer.MapResult;
import io.javaside.jharmonizer.Mapper;

public class WorkUserMapper implements Mapper<WorkUser, Long, FlatUser> {

    @Override
    public MapResult<Long, FlatUser> resultFrom(WorkUser user) {
        var flatUser = FlatUser.builder()
                .id(user.id())
                .name(user.name())
                .username(user.username())
                .email(user.email())
                .phone(user.phone())
                .website(user.website())
                .companyName(user.company().name())
                .catchPhrase(user.company().catchPhrase())
                .businessStrategy(user.company().bs())
                .build();
        return MapResult.of(flatUser.id(), flatUser);
    }

}
