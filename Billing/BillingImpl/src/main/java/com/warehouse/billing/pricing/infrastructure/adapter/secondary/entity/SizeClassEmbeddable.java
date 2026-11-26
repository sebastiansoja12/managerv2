package com.warehouse.billing.pricing.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Embeddable
public class SizeClassEmbeddable {

    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Column(name = "max_length_cm", nullable = false, precision = 10, scale = 2)
    private BigDecimal maxLengthCm;

    @Column(name = "max_width_cm", nullable = false, precision = 10, scale = 2)
    private BigDecimal maxWidthCm;

    @Column(name = "max_height_cm", nullable = false, precision = 10, scale = 2)
    private BigDecimal maxHeightCm;

    @Column(name = "net_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal netPrice;

    public SizeClassEmbeddable(final String code, final BigDecimal maxLengthCm, final BigDecimal maxWidthCm,
                               final BigDecimal maxHeightCm, final BigDecimal netPrice) {
        this.code = code;
        this.maxLengthCm = maxLengthCm;
        this.maxWidthCm = maxWidthCm;
        this.maxHeightCm = maxHeightCm;
        this.netPrice = netPrice;
    }
}
