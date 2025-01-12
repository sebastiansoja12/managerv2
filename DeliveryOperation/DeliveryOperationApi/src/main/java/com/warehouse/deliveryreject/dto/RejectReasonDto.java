package com.warehouse.deliveryreject.dto;

import java.util.Objects;

public record RejectReasonDto(String value) {

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RejectReasonDto that = (RejectReasonDto) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
