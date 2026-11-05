package com.warehouse.qrcode.domain.vo;

import java.math.BigDecimal;
import java.util.Locale;

public record ParcelDimensions(BigDecimal length, BigDecimal width, BigDecimal height, String unit) {

    public String format() {
        return plain(length) + " x " + plain(width) + " x " + plain(height) + " " + unit.toLowerCase(Locale.ROOT);
    }

    private static String plain(final BigDecimal value) {
        return value.stripTrailingZeros().toPlainString();
    }
}
