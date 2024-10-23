package com.warehouse.shipment.infrastructure.api.dto;

import java.math.BigDecimal;

public class MoneyDto {
    private BigDecimal amount;
    private String currency;

    public MoneyDto() {}

    public MoneyDto(final BigDecimal amount, final String currency) {
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
