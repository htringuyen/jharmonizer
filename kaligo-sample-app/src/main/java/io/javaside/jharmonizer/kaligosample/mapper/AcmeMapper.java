package io.javaside.jharmonizer.kaligosample.mapper;


import io.javaside.jharmonizer.MapResult;
import io.javaside.jharmonizer.Mapper;
import io.javaside.jharmonizer.kaligosample.model.Acme;
import io.javaside.jharmonizer.kaligosample.model.Kaligo;

import java.util.List;

public class AcmeMapper implements Mapper<Acme.Hotel, String, Kaligo.Hotel> {

    @Override
    public MapResult<String, Kaligo.Hotel> resultFrom(Acme.Hotel acme) {
        var kaligoHotel = Kaligo.Hotel.builder()
                .id(acme.id())
                .destinationId(acme.destinationId())
                .name(acme.name())
                .location(Kaligo.Location.builder()
                        .latitude(acme.latitude())
                        .longitude(acme.longitude())
                        .address(acme.address())
                        .city(acme.city())
                        .country(acme.country())
                        .build())
                .description(acme.description())
                .amenities(Kaligo.Amenities.builder()
                        .general(acme.facilities())
                        .room(List.of())
                        .build())
                .images(Kaligo.Images.builder()
                        .rooms(List.of())
                        .site(List.of())
                        .amenities(List.of())
                        .build())
                .bookingConditions(List.of())
                .build();
        return MapResult.of(kaligoHotel.id(), kaligoHotel);
    }
}
