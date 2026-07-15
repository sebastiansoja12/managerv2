package com.warehouse.voronoi.domain.vo;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public record GeocodingAddress(String city, String street, String postalCode) {

    public String formattedAddress() {
        return Stream.of(street, postalCode, city)
                .filter(value -> value != null && !value.isBlank())
                .map(String::trim)
                .collect(Collectors.joining(", "));
    }
}
