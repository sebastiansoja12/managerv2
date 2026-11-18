package com.warehouse.analytics.infrastructure.adapter.primary.api;

import java.math.BigDecimal;

public record CostBreakdownApiResponse(BigDecimal fuel,
                                       BigDecimal shipmentDamage,
                                       BigDecimal claims,
                                       BigDecimal total) {
}
