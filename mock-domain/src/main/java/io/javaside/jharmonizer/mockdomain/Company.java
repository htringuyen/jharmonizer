package io.javaside.jharmonizer.mockdomain;

import lombok.Builder;

@Builder
public record Company(String name, String catchPhrase, String bs) {}
