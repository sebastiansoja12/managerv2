package com.warehouse.billing.invoice.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Embeddable
public class InvoiceLineEmbeddable {

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "vat_percent", nullable = false, precision = 7, scale = 3)
    private BigDecimal vatPercent;

    @Column(name = "net", nullable = false, precision = 19, scale = 2)
    private BigDecimal net;

    @Column(name = "vat", nullable = false, precision = 19, scale = 2)
    private BigDecimal vat;

    @Column(name = "gross", nullable = false, precision = 19, scale = 2)
    private BigDecimal gross;

    public InvoiceLineEmbeddable(final String description, final BigDecimal vatPercent, final BigDecimal net,
                                 final BigDecimal vat, final BigDecimal gross) {
        this.description = description;
        this.vatPercent = vatPercent;
        this.net = net;
        this.vat = vat;
        this.gross = gross;
    }
}
