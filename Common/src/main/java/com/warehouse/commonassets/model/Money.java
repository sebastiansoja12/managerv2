package com.warehouse.commonassets.model;

import java.math.BigDecimal;

import com.warehouse.commonassets.enumeration.Currency;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class Money {
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private Currency currency;

    public Money() {

    }

    public Money(final BigDecimal amount, final Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public static Money from(final BigDecimal amount, final Currency currency) {
        return new Money(amount, currency);
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

    public boolean isDefined() {
        return amount != null && currency != null;
    }
}
