package com.warehouse.billing.pricing.infrastructure.adapter.primary.api;

import com.warehouse.billing.pricing.domain.enumeration.DeliveryOption;
import com.warehouse.billing.pricing.domain.enumeration.ServiceLevel;
import com.warehouse.commonassets.enumeration.Currency;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PriceQuoteApiRequest(@NotNull @Positive BigDecimal lengthCm, @NotNull @Positive BigDecimal widthCm,
                                   @NotNull @Positive BigDecimal heightCm, @NotNull @Positive BigDecimal weightKg,
                                   @NotNull ServiceLevel serviceLevel, @NotNull DeliveryOption deliveryOption,
                                   BigDecimal declaredValueAmount, Currency declaredValueCurrency) {
}
