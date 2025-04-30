package com.warehouse.commonassets.model;

import com.warehouse.commonassets.enumeration.Currency;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class Money {
    private BigDecimal amount;
    private Currency currency;

    public Money() {

    }

    public Money(final BigDecimal amount, final Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void changeCurrency(final Currency currency) {
        this.currency = currency;
    }
}
