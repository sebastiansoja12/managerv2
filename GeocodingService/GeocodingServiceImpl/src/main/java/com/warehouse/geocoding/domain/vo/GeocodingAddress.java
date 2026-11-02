package com.warehouse.geocoding.domain.vo;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public record GeocodingAddress(String city, String street, String postalCode, String countryCode) {

    public GeocodingAddress(final String city, final String street, final String postalCode) {
        this(city, street, postalCode, null);
    }

    public String formattedAddress() {
        return Stream.of(street, postalCode, city)
                .filter(value -> value != null && !value.isBlank())
                .map(String::trim)
                .collect(Collectors.joining(", "));
    }

    public NormalizedGeocodingAddress normalize() {
        return NormalizedGeocodingAddress.of(this);
    }
}
