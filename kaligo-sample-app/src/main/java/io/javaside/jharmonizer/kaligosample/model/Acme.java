package io.javaside.jharmonizer.kaligosample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

public interface Acme {

    @Builder
    record Hotel(
            @JsonProperty("Id") String id,
            @JsonProperty("DestinationId") Integer destinationId,
            @JsonProperty("Name") String name,
            @JsonProperty("Latitude") Double latitude,
            @JsonProperty("Longitude") Double longitude,
            @JsonProperty("Address") String address,
            @JsonProperty("City") String city,
            @JsonProperty("Country") String country,
            @JsonProperty("PostalCode") String postalCode,
            @JsonProperty("Description") String description,
            @JsonProperty("Facilities") List<String> facilities
    ) implements FilterableHotel {

        @Override
        public String getHotelId() {
            return id();
        }

        @Override
        public Integer getDestinationId() {
            return destinationId();
        }
    }
}
