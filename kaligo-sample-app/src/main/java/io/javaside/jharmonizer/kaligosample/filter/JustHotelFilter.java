package io.javaside.jharmonizer.kaligosample.filter;

import io.javaside.jharmonizer.Filter;
import io.javaside.jharmonizer.common.ConfigDef;
import io.javaside.jharmonizer.kaligosample.model.FilterableHotel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import static io.javaside.jharmonizer.common.MiscUtils.stringValueFrom;

public class JustHotelFilter implements Filter<FilterableHotel> {

    private static final Logger logger = LoggerFactory.getLogger(JustHotelFilter.class);

    public static final ConfigDef HOTEL_INCLUDE_LIST_DEF = ConfigDef.of("hotel.include.list", null);

    public static final ConfigDef DESTINATION_INCLUDE_LIST_DEF = ConfigDef.of("destination.include.list", null);

    private Set<String> includedHotels;

    private Set<Integer> includedDestinations;

    @Override
    public boolean filter(FilterableHotel hotel) {
        if (includedHotels == null
                || includedDestinations == null) {
            return true;
        }
        return hotelIncluded(hotel.getHotelId())
                && destinationIncluded(hotel.getDestinationId());
    }

    @Override
    public void configure(Properties props) {

        var hotels = stringValueFrom(props, HOTEL_INCLUDE_LIST_DEF);
        if (hotels != null && !hotels.isBlank()) {
            includedHotels = parseStringSet(hotels);
        }

        var destinations = stringValueFrom(props, DESTINATION_INCLUDE_LIST_DEF);
        if (destinations != null && !destinations.isBlank()) {
            includedDestinations = parseIntSet(destinations);
        }

        logger.debug("JustHotelFilter configured with included hotels: {} and included destinations: {}",
                includedHotels, includedDestinations);
    }

    private boolean hotelIncluded(String hotelId) {
        return includedHotels.contains(hotelId);
    }

    private boolean destinationIncluded(Integer destinationId) {
        return includedDestinations.contains(destinationId);
    }

    private Set<String> parseStringSet(String toParse) {
        return Arrays.stream(toParse.split(","))
                .map(String::strip)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toCollection(HashSet::new));
    }

    private Set<Integer> parseIntSet(String toParse) {
        return Arrays.stream(toParse.split(","))
                .map(String::strip)
                .filter(s -> !s.isBlank())
                .map(Integer::parseInt)
                .collect(Collectors.toCollection(HashSet::new));
    }


}
