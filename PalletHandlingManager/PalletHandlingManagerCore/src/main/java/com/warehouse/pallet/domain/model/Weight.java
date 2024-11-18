package com.warehouse.pallet.domain.model;

public class Weight {
    private Double value;

    public Weight(final Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public void addWeight(final Double value) {
        this.value += value;
    }
}
