package com.warehouse.shipment.domain.vo;

import java.math.BigDecimal;

public record Dimensions(
        BigDecimal length,
        BigDecimal width,
        BigDecimal height,
        LengthUnit unit
) {

    public Dimensions {
        requirePositive(length, "length");
        requirePositive(width, "width");
        requirePositive(height, "height");
        if (unit == null) {
            throw new IllegalArgumentException("unit is required");
        }
    }

    private static void requirePositive(final BigDecimal value, final String name) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(name + " must be greater than 0");
        }
    }
}
