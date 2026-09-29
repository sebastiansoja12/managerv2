package com.warehouse.shipment.domain.vo;

import java.math.BigDecimal;

public record Weight(BigDecimal value, WeightUnit unit) {

    public Weight {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("value must be greater than 0");
        }
        if (unit == null) {
            throw new IllegalArgumentException("unit is required");
        }
    }
}
