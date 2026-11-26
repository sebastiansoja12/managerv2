package com.warehouse.billing.api.dto;

import com.warehouse.commonassets.enumeration.Currency;

import java.math.BigDecimal;

public record PriceQuoteRequestDto(BigDecimal lengthCm, BigDecimal widthCm, BigDecimal heightCm,
                                   BigDecimal weightKg, ServiceLevelDto serviceLevel,
                                   DeliveryOptionDto deliveryOption, BigDecimal declaredValueAmount,
                                   Currency declaredValueCurrency) {
}
