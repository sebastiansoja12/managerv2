package com.warehouse.billing.pricing.infrastructure.adapter.primary.api;

import com.warehouse.billing.pricing.domain.enumeration.DeliveryOption;
import com.warehouse.billing.pricing.domain.enumeration.ServiceLevel;
import com.warehouse.commonassets.enumeration.Currency;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record ReplacePriceListApiRequest(@NotNull Currency currency,
                                         @NotEmpty List<@Valid @NotNull SizeClassApi> sizeClasses,
                                         @NotEmpty List<@Valid @NotNull WeightBandApi> weightBands,
                                         @NotEmpty Map<ServiceLevel, @NotNull @PositiveOrZero BigDecimal> serviceLevelMultipliers,
                                         @NotEmpty Map<DeliveryOption, @NotNull @PositiveOrZero BigDecimal> deliveryOptionSurcharges,
                                         @NotNull @PositiveOrZero BigDecimal insurancePercent,
                                         @NotNull @PositiveOrZero BigDecimal vatPercent) {
}
