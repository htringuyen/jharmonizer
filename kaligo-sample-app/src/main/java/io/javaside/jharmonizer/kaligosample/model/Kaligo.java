package io.javaside.jharmonizer.kaligosample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

public interface Kaligo {

    @Builder
    record Hotel(
            @JsonProperty("id") String id,
            @JsonProperty("destination_id") Integer destinationId,
            @JsonProperty("name") String name,
            @JsonProperty("location") Location location,
            @JsonProperty("description") String description,
            @JsonProperty("amenities") Amenities amenities,
            @JsonProperty("images") Images images,
            @JsonProperty("booking_conditions") List<String> bookingConditions
    ) {}

    @Builder
    record Location(
            @JsonProperty("lat") Double latitude,
            @JsonProperty("lng") Double longitude,
            @JsonProperty("address") String address,
            @JsonProperty("city") String city,
            @JsonProperty("country") String country
    ) {}

    @Builder
    record Amenities(
            @JsonProperty("general") List<String> general,
            @JsonProperty("room") List<String> room
    ) {}

    @Builder
    record Images(
            @JsonProperty("rooms") List<Image> rooms,
            @JsonProperty("site") List<Image> site,
            @JsonProperty("amenities") List<Image> amenities
    ) {}

    @Builder
    record Image(
            @JsonProperty("link") String link,
            @JsonProperty("description") String description
    ) {}
}

