package com.warehouse.billing.pricing.infrastructure.adapter.primary.api;

import com.warehouse.billing.pricing.domain.enumeration.DeliveryOption;
import com.warehouse.billing.pricing.domain.enumeration.ServiceLevel;
import com.warehouse.commonassets.enumeration.Currency;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public record PriceListApiResponse(PriceListIdDto priceListId, Currency currency, List<SizeClassApi> sizeClasses,
                                   List<WeightBandApi> weightBands,
                                   Map<ServiceLevel, BigDecimal> serviceLevelMultipliers,
                                   Map<DeliveryOption, BigDecimal> deliveryOptionSurcharges,
                                   BigDecimal insurancePercent, BigDecimal vatPercent, Instant updatedAt) {
}
