package com.warehouse.geocoding.domain.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class NormalizedGeocodingAddressTest {

    @Test
    void shouldNormalizeCaseAndWhitespace() {
        final NormalizedGeocodingAddress normalized =
                new GeocodingAddress("  KATOWICE ", " ul.   Mariacka    12/3 ", " 40-001", " pl ").normalize();

        assertEquals(new NormalizedGeocodingAddress("pl", "40-001", "katowice", "ul. mariacka", "12/3"), normalized);
    }

    @Test
    void shouldTreatLogicallyEqualAddressesAsSameKey() {
        assertEquals(
                new GeocodingAddress("Katowice", "Mariacka 12", "40-001", "PL").normalize(),
                new GeocodingAddress(" katowice", "MARIACKA   12 ", "40-001 ", "pl").normalize());
    }

    @Test
    void shouldUseEmptyStringsForMissingParts() {
        final NormalizedGeocodingAddress normalized = new GeocodingAddress("Katowice", null, null).normalize();

        assertEquals(new NormalizedGeocodingAddress("", "", "katowice", "", ""), normalized);
    }

    @Test
    void shouldKeepStreetWithoutHouseNumber() {
        final NormalizedGeocodingAddress normalized =
                new GeocodingAddress("Katowice", "Rynek", "40-001", "PL").normalize();

        assertEquals("rynek", normalized.street());
        assertEquals("", normalized.houseNumber());
    }
}
