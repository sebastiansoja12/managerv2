package com.warehouse.shipment.api.event.snapshot;

import java.math.BigDecimal;

import com.warehouse.commonassets.enumeration.Currency;

public record MoneySnapshot(BigDecimal amount, Currency currency) {
}
