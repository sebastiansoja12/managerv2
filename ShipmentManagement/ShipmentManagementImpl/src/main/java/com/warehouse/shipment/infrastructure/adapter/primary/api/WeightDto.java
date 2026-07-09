package com.warehouse.shipment.infrastructure.adapter.primary.api;

import java.math.BigDecimal;

public record WeightDto(BigDecimal value, String unit) {
}
