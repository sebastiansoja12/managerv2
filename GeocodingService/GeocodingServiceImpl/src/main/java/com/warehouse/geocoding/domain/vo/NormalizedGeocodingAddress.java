package com.warehouse.geocoding.domain.vo;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cache key for geocoded addresses. Every part is trimmed, has inner whitespace collapsed and is lower-cased;
 * missing parts become an empty string so the unique constraint on the table also covers them.
 */
public record NormalizedGeocodingAddress(String countryCode, String postalCode, String city, String street,
                                         String houseNumber) {

    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private static final Pattern STREET_WITH_HOUSE_NUMBER = Pattern.compile("^(.*\\S)\\s+(\\d\\S*)$");

    static NormalizedGeocodingAddress of(final GeocodingAddress address) {
        final String street = normalize(address.street());
        final Matcher matcher = STREET_WITH_HOUSE_NUMBER.matcher(street);
        final boolean hasHouseNumber = matcher.matches();
        return new NormalizedGeocodingAddress(
                normalize(address.countryCode()),
                normalize(address.postalCode()),
                normalize(address.city()),
                hasHouseNumber ? matcher.group(1) : street,
                hasHouseNumber ? matcher.group(2) : "");
    }

    private static String normalize(final String value) {
        if (value == null) {
            return "";
        }
        return WHITESPACE.matcher(value.trim()).replaceAll(" ").toLowerCase(Locale.ROOT);
    }
}
