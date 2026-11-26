package com.warehouse.billing.pricing.infrastructure.adapter.secondary.entity;

import com.warehouse.billing.pricing.domain.enumeration.PriceQuoteLineType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Embeddable
public class PriceQuoteLineEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private PriceQuoteLineType type;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "net", nullable = false, precision = 19, scale = 2)
    private BigDecimal net;

    @Column(name = "vat", nullable = false, precision = 19, scale = 2)
    private BigDecimal vat;

    @Column(name = "gross", nullable = false, precision = 19, scale = 2)
    private BigDecimal gross;

    public PriceQuoteLineEmbeddable(final PriceQuoteLineType type, final String description, final BigDecimal net,
                                    final BigDecimal vat, final BigDecimal gross) {
        this.type = type;
        this.description = description;
        this.net = net;
        this.vat = vat;
        this.gross = gross;
    }
}
