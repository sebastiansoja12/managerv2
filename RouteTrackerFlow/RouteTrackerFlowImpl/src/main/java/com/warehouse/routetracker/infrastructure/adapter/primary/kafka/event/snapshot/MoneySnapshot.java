package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot;

import java.math.BigDecimal;

import com.warehouse.commonassets.enumeration.Currency;

public record MoneySnapshot(BigDecimal amount, Currency currency) {
}
