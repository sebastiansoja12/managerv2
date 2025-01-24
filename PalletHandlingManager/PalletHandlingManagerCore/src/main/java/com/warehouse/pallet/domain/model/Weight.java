package com.warehouse.pallet.domain.model;

public class Weight {
    private Double value;
    private String unit;

    public Weight(final Double value, final String unit) {
        this.value = value;
        this.unit = unit;
    }

    public Double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(final String unit) {
        this.unit = unit;
    }

    public void addWeight(final Double value) {
        this.value += value;
    }
}
