package com.warehouse.pickuppoint.domain.vo;

import com.warehouse.pickuppoint.domain.exception.InvalidPickupPointConfigurationException;

public record ExternalPointReference(String networkCode, String pointCode) {

    public ExternalPointReference {
        networkCode = required(networkCode, "External network code", 60);
        pointCode = required(pointCode, "External point code", 120);
    }

    private static String required(final String value, final String field, final int maximumLength) {
        if (value == null || value.isBlank() || value.trim().length() > maximumLength) {
            throw new InvalidPickupPointConfigurationException(
                    field + " must contain 1 to " + maximumLength + " characters");
        }
        return value.trim();
    }
}
