package com.warehouse.billing.pricing.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record SizeClassApi(@NotBlank String code, @NotNull @Positive BigDecimal maxLengthCm,
                           @NotNull @Positive BigDecimal maxWidthCm, @NotNull @Positive BigDecimal maxHeightCm,
                           @NotNull @PositiveOrZero BigDecimal netPrice) {
}
