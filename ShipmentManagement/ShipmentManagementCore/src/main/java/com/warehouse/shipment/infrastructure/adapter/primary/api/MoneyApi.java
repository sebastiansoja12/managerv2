package com.warehouse.shipment.infrastructure.adapter.primary.api;

import java.math.BigDecimal;

public class MoneyApi {
    private BigDecimal amount;
    private String currency;

    public MoneyApi() {}

    public MoneyApi(final BigDecimal amount, final String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
