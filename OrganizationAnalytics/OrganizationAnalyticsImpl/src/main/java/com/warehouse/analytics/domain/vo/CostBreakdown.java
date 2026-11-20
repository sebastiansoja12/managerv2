package com.warehouse.analytics.domain.vo;

import com.warehouse.analytics.domain.enumeration.DepartmentCostCategory;

import java.math.BigDecimal;

public record CostBreakdown(BigDecimal fuel, BigDecimal shipmentDamage, BigDecimal claims) {

    public static final CostBreakdown NONE = new CostBreakdown(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

    public static CostBreakdown of(final DepartmentCostCategory category, final BigDecimal amount) {
        return switch (category) {
            case FUEL -> new CostBreakdown(amount, BigDecimal.ZERO, BigDecimal.ZERO);
            case SHIPMENT_DAMAGE -> new CostBreakdown(BigDecimal.ZERO, amount, BigDecimal.ZERO);
            case CLAIM -> new CostBreakdown(BigDecimal.ZERO, BigDecimal.ZERO, amount);
        };
    }

    public CostBreakdown plus(final CostBreakdown other) {
        return new CostBreakdown(fuel.add(other.fuel), shipmentDamage.add(other.shipmentDamage),
                claims.add(other.claims));
    }

    public BigDecimal total() {
        return fuel.add(shipmentDamage).add(claims);
    }
}
