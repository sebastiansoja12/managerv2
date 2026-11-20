package com.warehouse.analytics.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;

import java.math.BigDecimal;

public record DepartmentPerformance(DepartmentCode departmentCode,
                                    String city,
                                    long registeredShipments,
                                    long canceledShipments,
                                    long unpricedShipments,
                                    long deliveredShipments,
                                    BigDecimal revenue,
                                    BigDecimal actualTurnover,
                                    CostBreakdown costs,
                                    long costEntries,
                                    BigDecimal financialResult,
                                    BigDecimal profitabilityPercent,
                                    BigDecimal revenueSharePercent,
                                    long damagedShipments,
                                    long damageClaims) {
}
