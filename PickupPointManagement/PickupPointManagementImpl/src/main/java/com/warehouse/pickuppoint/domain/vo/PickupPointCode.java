package com.warehouse.pickuppoint.domain.vo;

import com.warehouse.pickuppoint.domain.exception.InvalidPickupPointConfigurationException;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public record PickupPointCode(String value) {

    private static final Pattern CODE_PATTERN = Pattern.compile("[A-Z0-9_-]{2,40}");

    public PickupPointCode {
        Objects.requireNonNull(value, "Pickup point code cannot be null");
        value = value.trim().toUpperCase(Locale.ROOT);
        if (!CODE_PATTERN.matcher(value).matches()) {
            throw new InvalidPickupPointConfigurationException(
                    "Pickup point code must contain 2 to 40 uppercase letters, digits, hyphens or underscores");
        }
    }
}
