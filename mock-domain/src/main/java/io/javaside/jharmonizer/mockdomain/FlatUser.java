package io.javaside.jharmonizer.mockdomain;

import lombok.Builder;

@Builder
public record FlatUser(Long id, String name, String username, String email,
                       String street, String suite, String city, String zipcode,
                       Double latitude, Double longitude,
                       String phone, String website,
                       String companyName, String catchPhrase, String businessStrategy) {
}
