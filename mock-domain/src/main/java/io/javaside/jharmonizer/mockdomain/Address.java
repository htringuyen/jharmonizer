package io.javaside.jharmonizer.mockdomain;

import lombok.Builder;

@Builder
public record Address(String street, String suite, String city, String zipcode, Geographic geo) {
}
