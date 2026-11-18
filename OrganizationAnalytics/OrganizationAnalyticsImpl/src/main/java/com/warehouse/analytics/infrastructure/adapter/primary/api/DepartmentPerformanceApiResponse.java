package com.warehouse.analytics.infrastructure.adapter.primary.api;

import java.math.BigDecimal;

public record DepartmentPerformanceApiResponse(String departmentCode,
                                               String city,
                                               long registeredShipments,
                                               long canceledShipments,
                                               long unpricedShipments,
                                               long deliveredShipments,
                                               BigDecimal revenue,
                                               BigDecimal actualTurnover,
                                               CostBreakdownApiResponse costs,
                                               long costEntries,
                                               BigDecimal financialResult,
                                               BigDecimal profitabilityPercent,
                                               BigDecimal revenueSharePercent,
                                               long damagedShipments,
                                               long damageClaims) {
}
