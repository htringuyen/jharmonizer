package io.javaside.jharmonizer.mockdomain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LifeUser(Long id, String name, String username,
                       String email, String phone,
                       Address address) {
}
