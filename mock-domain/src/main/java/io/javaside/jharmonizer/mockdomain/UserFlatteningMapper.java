package io.javaside.jharmonizer.mockdomain;

import io.javaside.jharmonizer.Mapper;
import io.javaside.jharmonizer.MapResult;

public class UserFlatteningMapper implements Mapper<User, Long, FlatUser> {

    @Override
    public MapResult<Long, FlatUser> resultFrom(User user) {
        var flatUser = FlatUser.builder()
                .id(user.id())
                .name(user.name())
                .username(user.username())
                .email(user.email())
                .street(user.address().street())
                .suite(user.address().suite())
                .city(user.address().city())
                .zipcode(user.address().zipcode())
                .latitude(user.address().geo().lat())
                .longitude(user.address().geo().lng())
                .phone(user.phone())
                .website(user.website())
                .companyName(user.company().name())
                .catchPhrase(user.company().catchPhrase())
                .businessStrategy(user.company().bs())
                .build();
        return MapResult.of(flatUser.id(), flatUser);
    }
}
