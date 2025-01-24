package com.warehouse.pallet.domain.vo;

import java.util.Objects;

public class MaxPalletWeight {
    private final Double value;
    private final String unit;

    public MaxPalletWeight(final Double value, final String unit) {
        this.value = value;
        this.unit = unit;
    }

    public Double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final MaxPalletWeight that = (MaxPalletWeight) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
