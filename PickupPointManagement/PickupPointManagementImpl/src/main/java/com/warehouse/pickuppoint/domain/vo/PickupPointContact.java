package com.warehouse.pickuppoint.domain.vo;

import com.warehouse.pickuppoint.domain.exception.InvalidPickupPointConfigurationException;

public record PickupPointContact(String telephoneNumber, String email) {

    public PickupPointContact {
        telephoneNumber = optional(telephoneNumber, "Telephone number", 40);
        email = optional(email, "Email", 254);
        if (telephoneNumber == null && email == null) {
            throw new InvalidPickupPointConfigurationException(
                    "Pickup point contact must contain a telephone number or email");
        }
    }

    private static String optional(final String value, final String field, final int maximumLength) {
        if (value == null || value.isBlank()) {
            return null;
        }
        final String normalized = value.trim();
        if (normalized.length() > maximumLength) {
            throw new InvalidPickupPointConfigurationException(
                    field + " cannot exceed " + maximumLength + " characters");
        }
        return normalized;
    }
}
