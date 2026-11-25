package com.warehouse.billing.pricing.domain.vo;

import com.warehouse.billing.pricing.domain.enumeration.DeliveryOption;
import com.warehouse.billing.pricing.domain.enumeration.ServiceLevel;
import com.warehouse.commonassets.model.Money;

import java.math.BigDecimal;

public record PricingInput(BigDecimal lengthCm, BigDecimal widthCm, BigDecimal heightCm, BigDecimal weightKg,
                           ServiceLevel serviceLevel, DeliveryOption deliveryOption, Money declaredValue) {
}
