package com.warehouse.shipment.infrastructure.adapter.primary.api;

import java.math.BigDecimal;

public record WeightApi(BigDecimal value, WeightUnitDto unit) {
}
