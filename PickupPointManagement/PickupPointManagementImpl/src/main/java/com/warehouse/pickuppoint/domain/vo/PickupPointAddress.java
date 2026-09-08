package com.warehouse.pickuppoint.domain.vo;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.pickuppoint.domain.exception.InvalidPickupPointConfigurationException;

import java.util.Objects;

public record PickupPointAddress(
        CountryCode countryCode,
        String postalCode,
        String city,
        String street,
        String buildingNumber,
        String unitNumber) {

    public PickupPointAddress {
        Objects.requireNonNull(countryCode, "Country code cannot be null");
        postalCode = required(postalCode, "Postal code", 20);
        city = required(city, "City", 120);
        street = required(street, "Street", 160);
        buildingNumber = required(buildingNumber, "Building number", 30);
        unitNumber = optional(unitNumber, "Unit number", 30);
    }

    private static String required(final String value, final String field, final int maximumLength) {
        if (value == null || value.isBlank() || value.trim().length() > maximumLength) {
            throw new InvalidPickupPointConfigurationException(
                    field + " must contain 1 to " + maximumLength + " characters");
        }
        return value.trim();
    }

    private static String optional(final String value, final String field, final int maximumLength) {
        if (value == null) {
            return null;
        }
        final String normalized = value.trim();
        if (normalized.length() > maximumLength) {
            throw new InvalidPickupPointConfigurationException(
                    field + " cannot exceed " + maximumLength + " characters");
        }
        return normalized.isEmpty() ? null : normalized;
    }
}
