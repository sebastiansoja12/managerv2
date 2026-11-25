package com.warehouse.billing.pricing.domain.vo;

import java.math.BigDecimal;

public record WeightBand(BigDecimal fromKgInclusive, BigDecimal toKgExclusive, BigDecimal netSurcharge) {

    public boolean contains(final BigDecimal weightKg) {
        return weightKg.compareTo(fromKgInclusive) >= 0 && weightKg.compareTo(toKgExclusive) < 0;
    }
}
