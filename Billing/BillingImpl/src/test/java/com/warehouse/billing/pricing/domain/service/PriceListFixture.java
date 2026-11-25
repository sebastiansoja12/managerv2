package com.warehouse.billing.pricing.domain.service;

import com.warehouse.billing.pricing.domain.enumeration.DeliveryOption;
import com.warehouse.billing.pricing.domain.enumeration.ServiceLevel;
import com.warehouse.billing.pricing.domain.model.PriceList;
import com.warehouse.billing.pricing.domain.vo.PricingInput;
import com.warehouse.billing.pricing.domain.vo.SizeClass;
import com.warehouse.billing.pricing.domain.vo.WeightBand;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PriceListId;
import com.warehouse.commonassets.model.Money;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public final class PriceListFixture {

    private PriceListFixture() {
    }

    public static PriceList defaultPln() {
        return defaultPln(new OperatorId(1L));
    }

    public static PriceList defaultPln(final OperatorId operatorId) {
        return new PriceList(new PriceListId(1L), operatorId, Currency.PLN,
                List.of(sizeClass("S", 8, 38, 64, "12.00"), sizeClass("M", 19, 38, 64, "14.00"),
                        sizeClass("L", 41, 38, 64, "17.00"), sizeClass("XL", 80, 60, 120, "39.00")),
                List.of(weightBand(0, 5, "0"), weightBand(5, 10, "3.00"), weightBand(10, 25, "8.00"),
                        weightBand(25, 50, "20.00")),
                Map.of(ServiceLevel.ECONOMY, new BigDecimal("0.90"), ServiceLevel.STANDARD, new BigDecimal("1.00"),
                        ServiceLevel.EXPRESS, new BigDecimal("1.50")),
                Map.of(DeliveryOption.COURIER, new BigDecimal("4.00"), DeliveryOption.PICKUP_POINT, BigDecimal.ZERO,
                        DeliveryOption.LOCKER, BigDecimal.ZERO),
                new BigDecimal("1.0"), new BigDecimal("23"), Instant.parse("2026-11-24T10:00:00Z"));
    }

    public static PricingInput input(final double lengthCm, final double widthCm, final double heightCm,
                              final double weightKg, final ServiceLevel serviceLevel,
                              final DeliveryOption deliveryOption, final Money declaredValue) {
        return new PricingInput(BigDecimal.valueOf(lengthCm), BigDecimal.valueOf(widthCm),
                BigDecimal.valueOf(heightCm), BigDecimal.valueOf(weightKg), serviceLevel, deliveryOption,
                declaredValue);
    }

    private static SizeClass sizeClass(final String code, final int lengthCm, final int widthCm,
                                       final int heightCm, final String netPrice) {
        return new SizeClass(code, BigDecimal.valueOf(lengthCm), BigDecimal.valueOf(widthCm),
                BigDecimal.valueOf(heightCm), new BigDecimal(netPrice));
    }

    private static WeightBand weightBand(final int fromKg, final int toKg, final String netSurcharge) {
        return new WeightBand(BigDecimal.valueOf(fromKg), BigDecimal.valueOf(toKg), new BigDecimal(netSurcharge));
    }
}
