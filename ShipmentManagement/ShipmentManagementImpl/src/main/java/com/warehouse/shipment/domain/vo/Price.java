package com.warehouse.shipment.domain.vo;

import java.math.BigDecimal;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.model.Money;

public record Price(BigDecimal price, Currency currency) {
    public Price(final Money money) {
        this(money.getAmount(), money.getCurrency());
    }

    public static Price empty() {
        return new Price(BigDecimal.ZERO, Currency.PLN);
    }


    public Money getMoney() {
        return new Money(price, currency);
    }
}
