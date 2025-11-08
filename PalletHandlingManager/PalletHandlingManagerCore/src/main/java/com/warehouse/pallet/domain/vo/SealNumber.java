package com.warehouse.pallet.domain.vo;

import java.util.Objects;

public record SealNumber(String value) {

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final SealNumber that = (SealNumber) o;
        return Objects.equals(value, that.value);
    }

}
