package com.warehouse.pallet.domain.model;

import com.warehouse.pallet.domain.enumeration.Unit;

import java.math.BigDecimal;

public class Weight {
    private BigDecimal value;
    private Unit unit;

    public Weight(final BigDecimal value, final Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(final Unit unit) {
        this.unit = unit;
    }

    public void setValue(final BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
