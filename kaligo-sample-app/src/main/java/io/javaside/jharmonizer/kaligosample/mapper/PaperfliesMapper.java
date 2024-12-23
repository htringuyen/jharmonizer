package io.javaside.jharmonizer.kaligosample.mapper;

import io.javaside.jharmonizer.MapResult;
import io.javaside.jharmonizer.Mapper;
import io.javaside.jharmonizer.kaligosample.model.Kaligo;
import io.javaside.jharmonizer.kaligosample.model.Paperflies;

import java.util.List;

public class PaperfliesMapper implements Mapper<Paperflies.Hotel, String, Kaligo.Hotel> {

    @Override
    public MapResult<String, Kaligo.Hotel> resultFrom(Paperflies.Hotel paperflies) {
        var kaligoHotel = Kaligo.Hotel.builder()
                .id(paperflies.hotelId())
                .destinationId(paperflies.destinationId())
                .name(paperflies.hotelName())
                .location(Kaligo.Location.builder()
                        .address(paperflies.location().address())
                        .country(paperflies.location().country())
                        .build())
                .description(paperflies.details())
                .amenities(Kaligo.Amenities.builder()
                        .general(paperflies.amenities().general())
                        .room(paperflies.amenities().room())
                        .build())
                .images(Kaligo.Images.builder()
                        .rooms(paperflies.images().rooms().stream()
                                .map(this::toKaligoImage)
                                .toList())
                        .amenities(List.of())
                        .site(paperflies.images().site().stream()
                                .map(this::toKaligoImage)
                                .toList())
                        .build())
                .bookingConditions(paperflies.bookingConditions())
                .build();
        return MapResult.of(kaligoHotel.id(), kaligoHotel);
    }

    private Kaligo.Image toKaligoImage(Paperflies.Image image) {
        return Kaligo.Image.builder()
                .link(image.link())
                .description(image.caption())
                .build();
    }
}
