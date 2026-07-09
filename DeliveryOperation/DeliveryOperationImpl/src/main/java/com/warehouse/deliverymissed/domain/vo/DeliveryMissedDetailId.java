package com.warehouse.deliverymissed.domain.vo;

import java.util.Objects;

public record DeliveryMissedDetailId(Long value) {

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final DeliveryMissedDetailId that = (DeliveryMissedDetailId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
