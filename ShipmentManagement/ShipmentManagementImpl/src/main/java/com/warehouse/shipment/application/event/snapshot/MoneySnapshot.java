package com.warehouse.shipment.application.event.snapshot;

import java.math.BigDecimal;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.model.Money;

public record MoneySnapshot(BigDecimal amount, Currency currency) {

    public static MoneySnapshot from(final Money money) {
        if (money == null) {
            return null;
        }
        return new MoneySnapshot(money.getAmount(), money.getCurrency());
    }
}
