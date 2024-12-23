package io.javaside.jharmonizer.mockdomain;

import lombok.Builder;

@Builder
public record Geographic(double lat, double lng) {
}
