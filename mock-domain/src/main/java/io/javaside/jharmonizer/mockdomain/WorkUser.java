package io.javaside.jharmonizer.mockdomain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WorkUser(Long id, String name, String username,
                       String email, String phone, String website, Company company) {}
