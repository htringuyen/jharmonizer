package io.javaside.jharmonizer.kaligosample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

public interface Patagonia {

    @Builder
    record Hotel(
            @JsonProperty("id") String id,
            @JsonProperty("destination") Integer destination,
            @JsonProperty("name") String name,
            @JsonProperty("lat") Double latitude,
            @JsonProperty("lng") Double longitude,
            @JsonProperty("address") String address,
            @JsonProperty("info") String info,
            @JsonProperty("amenities") List<String> amenities,
            @JsonProperty("images") Images images
    ) implements FilterableHotel {

        @Override
        public String getHotelId() {
            return id();
        }

        @Override
        public Integer getDestinationId() {
            return destination();
        }

    }

    @Builder
    record Images(
            @JsonProperty("rooms") List<Image> rooms,
            @JsonProperty("amenities") List<Image> amenities
    ) {}

    @Builder
    record Image(
            @JsonProperty("url") String url,
            @JsonProperty("description") String description
    ) {}
}

