package com.warehouse.pallet.domain.vo;

import com.warehouse.pallet.domain.enumeration.Unit;

import java.math.BigDecimal;
import java.util.Objects;

public class MaxPalletWeight {
    private final BigDecimal value;
    private final Unit unit;

    public MaxPalletWeight(final BigDecimal value, final Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Unit getUnit() {
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
