package com.warehouse.billing.pricing.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Embeddable
public class WeightBandEmbeddable {

    @Column(name = "from_kg", nullable = false, precision = 10, scale = 3)
    private BigDecimal fromKgInclusive;

    @Column(name = "to_kg", nullable = false, precision = 10, scale = 3)
    private BigDecimal toKgExclusive;

    @Column(name = "net_surcharge", nullable = false, precision = 19, scale = 2)
    private BigDecimal netSurcharge;

    public WeightBandEmbeddable(final BigDecimal fromKgInclusive, final BigDecimal toKgExclusive,
                                final BigDecimal netSurcharge) {
        this.fromKgInclusive = fromKgInclusive;
        this.toKgExclusive = toKgExclusive;
        this.netSurcharge = netSurcharge;
    }
}
