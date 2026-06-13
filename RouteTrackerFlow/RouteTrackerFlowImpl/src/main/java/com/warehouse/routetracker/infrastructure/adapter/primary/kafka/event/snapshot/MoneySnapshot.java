package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot;

import java.math.BigDecimal;

public record MoneySnapshot(BigDecimal amount, Currency currency) {
}
