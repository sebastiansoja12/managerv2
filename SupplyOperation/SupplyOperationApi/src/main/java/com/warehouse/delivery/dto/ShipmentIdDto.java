package com.warehouse.delivery.dto;

import java.util.Objects;

public record ShipmentIdDto(Long value) {

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ShipmentIdDto that = (ShipmentIdDto) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
