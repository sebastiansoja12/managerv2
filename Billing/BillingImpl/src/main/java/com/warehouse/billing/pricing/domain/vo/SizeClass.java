package com.warehouse.billing.pricing.domain.vo;

import java.math.BigDecimal;
import java.util.stream.Stream;

public record SizeClass(String code, BigDecimal maxLengthCm, BigDecimal maxWidthCm, BigDecimal maxHeightCm,
                        BigDecimal netPrice) {

    public boolean fits(final BigDecimal lengthCm, final BigDecimal widthCm, final BigDecimal heightCm) {
        final BigDecimal[] shipment = sorted(lengthCm, widthCm, heightCm);
        final BigDecimal[] limits = sorted(maxLengthCm, maxWidthCm, maxHeightCm);
        for (int index = 0; index < shipment.length; index++) {
            if (shipment[index].compareTo(limits[index]) > 0) {
                return false;
            }
        }
        return true;
    }

    private static BigDecimal[] sorted(final BigDecimal first, final BigDecimal second, final BigDecimal third) {
        return Stream.of(first, second, third).sorted().toArray(BigDecimal[]::new);
    }
}
