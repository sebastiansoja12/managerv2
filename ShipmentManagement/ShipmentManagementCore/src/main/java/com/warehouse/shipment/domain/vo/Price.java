package com.warehouse.shipment.domain.vo;

import java.math.BigDecimal;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.PriceEntity;

public record Price(BigDecimal price, Currency currency) {
    public static Price from(PriceEntity priceEntity) {
        return new Price(priceEntity.getPrice().getAmount(), priceEntity.getPrice().getCurrency());
    }

    public static Price empty() {
        return new Price(BigDecimal.ZERO, Currency.PLN);
    }


    public Money getMoney() {
        return new Money(price, currency);
    }
}
