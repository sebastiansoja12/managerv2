package com.warehouse.billing.invoice.domain.vo;

import com.warehouse.commonassets.model.Money;

import java.math.BigDecimal;

public record InvoiceLine(String description, BigDecimal vatPercent, Money net, Money vat, Money gross) {

    public InvoiceLine negate() {
        return new InvoiceLine(description, vatPercent, net.negate(), vat.negate(), gross.negate());
    }
}
