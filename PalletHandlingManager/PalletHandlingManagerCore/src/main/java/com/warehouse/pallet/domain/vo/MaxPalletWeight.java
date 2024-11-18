package com.warehouse.pallet.domain.vo;

import java.util.Objects;

public class MaxPalletWeight {
    private final Double value;

    public MaxPalletWeight(final Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
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
