package com.warehouse.deliveryreject.domain.vo;

import java.util.Objects;

public record RejectReasonId(Long value) {

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final RejectReasonId reasonId = (RejectReasonId) o;
        return Objects.equals(value, reasonId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
