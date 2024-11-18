package com.warehouse.pallet.domain.vo;

import java.util.Objects;
import java.util.UUID;

public class SealNumber {
    private final UUID value;

    public SealNumber(final UUID value) {
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final SealNumber that = (SealNumber) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
