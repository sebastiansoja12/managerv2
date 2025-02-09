package com.warehouse.pallet.domain.model;

import com.warehouse.pallet.domain.enumeration.Unit;

public class Weight {
    private Double value;
    private Unit unit;

    public Weight(final Double value, final Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(final Unit unit) {
        this.unit = unit;
    }

    public void addWeight(final Double value) {
        this.value += value;
    }
}
