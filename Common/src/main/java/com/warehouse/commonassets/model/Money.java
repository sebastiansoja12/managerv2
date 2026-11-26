package com.warehouse.commonassets.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.warehouse.commonassets.enumeration.Currency;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class Money implements Comparable<Money> {

    private static final int SCALE = 2;

    private static final BigDecimal HUNDRED = new BigDecimal("100");

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

    public Money add(final Money other) {
        requireSameCurrency(other);
        return monetary(this.amount.add(other.amount));
    }

    public Money subtract(final Money other) {
        requireSameCurrency(other);
        return monetary(this.amount.subtract(other.amount));
    }

    public Money multiply(final BigDecimal factor) {
        return monetary(this.amount.multiply(factor));
    }

    public Money percentage(final BigDecimal percent) {
        return monetary(this.amount.multiply(percent).divide(HUNDRED, SCALE, RoundingMode.HALF_UP));
    }

    public Money negate() {
        return monetary(this.amount.negate());
    }

    public boolean isZero() {
        return this.amount.signum() == 0;
    }

    public boolean isPositive() {
        return this.amount.signum() > 0;
    }

    @Override
    public int compareTo(final Money other) {
        requireSameCurrency(other);
        return this.amount.compareTo(other.amount);
    }

    private Money monetary(final BigDecimal value) {
        return new Money(value.setScale(SCALE, RoundingMode.HALF_UP), this.currency);
    }

    private void requireSameCurrency(final Money other) {
        if (this.currency != other.currency) {
            throw new IllegalArgumentException("Money currencies differ: " + this.currency + " and "
                    + other.currency);
        }
    }
}
