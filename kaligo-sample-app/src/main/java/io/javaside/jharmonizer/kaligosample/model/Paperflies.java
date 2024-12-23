package io.javaside.jharmonizer.kaligosample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

public interface Paperflies {

    @Builder
    record Hotel(
            @JsonProperty("hotel_id") String hotelId,
            @JsonProperty("destination_id") Integer destinationId,
            @JsonProperty("hotel_name") String hotelName,
            @JsonProperty("location") Location location,
            @JsonProperty("details") String details,
            @JsonProperty("amenities") Amenities amenities,
            @JsonProperty("images") Images images,
            @JsonProperty("booking_conditions") List<String> bookingConditions
    ) implements FilterableHotel {

        @Override
        public String getHotelId() {
            return hotelId();
        }

        @Override
        public Integer getDestinationId() {
            return destinationId();
        }
    }

    @Builder
    record Location(
            @JsonProperty("address") String address,
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
            @JsonProperty("site") List<Image> site
    ) {}

    @Builder
    record Image(
            @JsonProperty("link") String link,
            @JsonProperty("caption") String caption
    ) {}
}
