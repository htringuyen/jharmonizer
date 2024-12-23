package io.javaside.jharmonizer.kaligosample.mapper;

import io.javaside.jharmonizer.MapResult;
import io.javaside.jharmonizer.Mapper;
import io.javaside.jharmonizer.kaligosample.model.Kaligo;
import io.javaside.jharmonizer.kaligosample.model.Patagonia;

import java.util.List;
import java.util.stream.Collectors;

public class PatagoniaMapper implements Mapper<Patagonia.Hotel, String, Kaligo.Hotel> {

    @Override
    public MapResult<String, Kaligo.Hotel> resultFrom(Patagonia.Hotel patagonia) {
        var kaligoHotel = Kaligo.Hotel.builder()
                .id(patagonia.id())
                .destinationId(patagonia.destination())
                .name(patagonia.name())
                .location(Kaligo.Location.builder()
                        .latitude(patagonia.latitude())
                        .longitude(patagonia.longitude())
                        .address(patagonia.address())
                        .build())
                .description(patagonia.info())
                .amenities(Kaligo.Amenities.builder()
                        .general(patagonia.amenities())
                        .room(List.of())
                        .build())
                .images(Kaligo.Images.builder()
                        .rooms(patagonia.images().rooms().stream()
                                .map(this::toKaligoImage)
                                .toList())
                        .site(List.of())
                        .amenities(patagonia.images().amenities().stream()
                                .map(this::toKaligoImage)
                                .toList())
                        .build())
                .bookingConditions(List.of())
                .build();
        return MapResult.of(kaligoHotel.id(), kaligoHotel);
    }

    private Kaligo.Image toKaligoImage(Patagonia.Image image) {
        return Kaligo.Image.builder()
                .link(image.url())
                .description(image.description())
                .build();
    }
}
