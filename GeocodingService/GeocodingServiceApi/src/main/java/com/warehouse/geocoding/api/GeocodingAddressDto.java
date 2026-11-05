package com.warehouse.geocoding.api;

public record GeocodingAddressDto(String city, String street, String postalCode, String countryCode) {

    public GeocodingAddressDto(final String city, final String street, final String postalCode) {
        this(city, street, postalCode, null);
    }
}
