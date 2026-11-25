package com.warehouse.billing.pricing.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record WeightBandApi(@NotNull @PositiveOrZero BigDecimal fromKgInclusive,
                            @NotNull @PositiveOrZero BigDecimal toKgExclusive,
                            @NotNull @PositiveOrZero BigDecimal netSurcharge) {
}
