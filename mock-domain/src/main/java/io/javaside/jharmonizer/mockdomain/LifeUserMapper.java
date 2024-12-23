package io.javaside.jharmonizer.mockdomain;

import io.javaside.jharmonizer.MapResult;
import io.javaside.jharmonizer.Mapper;

public class LifeUserMapper implements Mapper<LifeUser, Long, FlatUser> {

    @Override
    public MapResult<Long, FlatUser> resultFrom(LifeUser user) {
        var flatUser = FlatUser.builder()
                .id(user.id())
                .name(user.name())
                .username(user.username())
                .email(user.email())
                .phone(user.phone())
                .street(user.address().street())
                .suite(user.address().suite())
                .city(user.address().city())
                .zipcode(user.address().zipcode())
                .latitude(user.address().geo().lat())
                .longitude(user.address().geo().lng())
                .build();
        return MapResult.of(flatUser.id(), flatUser);
    }
}
