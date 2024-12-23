package io.javaside.jharmonizer.kaligosample.merge;

import io.javaside.jharmonizer.AbstractMerge;
import io.javaside.jharmonizer.HarmonizedRecord;
import io.javaside.jharmonizer.SourceRecord;
import io.javaside.jharmonizer.kaligosample.model.Kaligo;

import java.util.*;

public class PriorityMerge extends AbstractMerge<String, Kaligo.Hotel> {

    private static final FieldDef<String> ID_FIELD = FieldDef.of("id", String.class);

    private static final FieldDef<Integer> DESTINATION_ID_FIELD = FieldDef.of("destinationId", Integer.class);

    private static final FieldDef<String> NAME_FIELD = FieldDef.of("name", String.class);

    private static final FieldDef<Double> LOCATION_LATITUDE_FIELD = FieldDef.of("location.latitude", Double.class);

    private static final FieldDef<Double> LOCATION_LONGITUDE_FIELD = FieldDef.of("location.longitude", Double.class);

    private static final FieldDef<String> LOCATION_ADDRESS_FIELD = FieldDef.of("location.address", String.class);

    private static final FieldDef<String> LOCATION_CITY_FIELD = FieldDef.of("location.city", String.class);

    private static final FieldDef<String> LOCATION_COUNTRY_FIELD = FieldDef.of("location.country", String.class);

    private static final FieldDef<String> DESCRIPTION_FIELD = FieldDef.of("description", String.class);

    private static final FieldDef<List<String>> AMENITIES_GENERAL_FIELD = FieldDef.ofList("amenities.general", String.class);

    private static final FieldDef<List<String>> AMENITIES_ROOM_FIELD = FieldDef.ofList("amenities.room", String.class);

    private static final FieldDef<List<Kaligo.Image>> IMAGES_ROOMS_FIELD = FieldDef.ofList("images.rooms", Kaligo.Image.class);

    private static final FieldDef<List<Kaligo.Image>> IMAGES_SITE_FIELD = FieldDef.ofList("images.site", Kaligo.Image.class);

    private static final FieldDef<List<Kaligo.Image>> IMAGES_AMENITIES_FIELD = FieldDef.ofList("images.amenities", Kaligo.Image.class);

    private static final FieldDef<List<String>> BOOKING_CONDITIONS_FIELD = FieldDef.ofList("bookingConditions", String.class);

    private final Map<String, PriorityField> fieldMap = new HashMap<>();

    private final Map<String, Map<String, Integer>> fieldSourcePriorityMap = new HashMap<>();


    @Override
    public HarmonizedRecord<String, Kaligo.Hotel> flush() {
        var builder = Kaligo.Hotel.builder();

        builder.id(valueOf(ID_FIELD));
        builder.destinationId(valueOf(DESTINATION_ID_FIELD));
        builder.name(valueOf(NAME_FIELD));

        builder.location(Kaligo.Location.builder()
                .latitude(valueOf(LOCATION_LATITUDE_FIELD))
                .longitude(valueOf(LOCATION_LONGITUDE_FIELD))
                .address(valueOf(LOCATION_ADDRESS_FIELD))
                .city(valueOf(LOCATION_CITY_FIELD))
                .country(valueOf(LOCATION_COUNTRY_FIELD))
                .build());

        builder.description(valueOf(DESCRIPTION_FIELD));

        builder.amenities(Kaligo.Amenities.builder()
                .general(valueOf(AMENITIES_GENERAL_FIELD))
                .room(valueOf(AMENITIES_ROOM_FIELD))
                .build());

        builder.images(Kaligo.Images.builder()
                .rooms(valueOf(IMAGES_ROOMS_FIELD))
                .site(valueOf(IMAGES_SITE_FIELD))
                .amenities(valueOf(IMAGES_AMENITIES_FIELD))
                .build());

        builder.bookingConditions(valueOf(BOOKING_CONDITIONS_FIELD));

        var hotel = builder.build();
        return HarmonizedRecord.of(hotel.id(), hotel);
    }

    private <T> T valueOf(FieldDef<T> fieldDef) {
        return fieldDef.parseValue(fieldMap.get(fieldDef.getName()).getValue());
    }

    @Override
    protected final void doCombine(SourceRecord<String, Kaligo.Hotel> record) {

        String sourceId = record.getSourceId();

        var hotel = record.getValue();

        writeField(ID_FIELD, hotel.id(), sourceId);

        writeField(DESTINATION_ID_FIELD, hotel.destinationId(), sourceId);

        writeField(NAME_FIELD, hotel.name(), sourceId);

        writeField(LOCATION_LATITUDE_FIELD, hotel.location().latitude(), sourceId);

        writeField(LOCATION_LONGITUDE_FIELD, hotel.location().longitude(), sourceId);

        writeField(LOCATION_ADDRESS_FIELD, hotel.location().address(), sourceId);

        writeField(LOCATION_CITY_FIELD, hotel.location().city(), sourceId);

        writeField(LOCATION_COUNTRY_FIELD, hotel.location().country(), sourceId);

        writeField(DESCRIPTION_FIELD, hotel.description(), sourceId);

        writeField(AMENITIES_GENERAL_FIELD, hotel.amenities().general(), sourceId);

        writeField(AMENITIES_ROOM_FIELD, hotel.amenities().room(), sourceId);

        writeField(IMAGES_ROOMS_FIELD, hotel.images().rooms(), sourceId);

        writeField(IMAGES_SITE_FIELD, hotel.images().site(), sourceId);

        writeField(IMAGES_AMENITIES_FIELD, hotel.images().amenities(), sourceId);

        writeField(BOOKING_CONDITIONS_FIELD, hotel.bookingConditions(), sourceId);
    }

    private <T> void writeField(FieldDef<T> fieldDef, T value, String sourceId) {

        var fieldName = fieldDef.getName();
        var field = fieldMap.get(fieldDef.getName());

        if (field == null) {
            field = PriorityField.of(fieldName, fieldSourcePriorityMap.get(fieldName));
            fieldMap.put(fieldName, field);
        }

        field.writeField(sourceId, value);
    }
}


















