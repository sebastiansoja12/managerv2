package com.warehouse.billing.pricing.application.port.primary.command;

import com.warehouse.billing.pricing.domain.enumeration.DeliveryOption;
import com.warehouse.billing.pricing.domain.enumeration.ServiceLevel;
import com.warehouse.billing.pricing.domain.vo.SizeClass;
import com.warehouse.billing.pricing.domain.vo.WeightBand;
import com.warehouse.commonassets.enumeration.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record ReplacePriceListCommand(Currency currency, List<SizeClass> sizeClasses, List<WeightBand> weightBands,
                                      Map<ServiceLevel, BigDecimal> serviceLevelMultipliers,
                                      Map<DeliveryOption, BigDecimal> deliveryOptionSurcharges,
                                      BigDecimal insurancePercent, BigDecimal vatPercent) {
}
