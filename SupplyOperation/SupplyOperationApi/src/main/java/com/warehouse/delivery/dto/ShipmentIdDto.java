package com.warehouse.delivery.dto;

import java.util.Objects;

import com.warehouse.commonassets.identificator.ShipmentId;

public record ShipmentIdDto(Long value) {

    public static ShipmentIdDto from(final ShipmentId shipmentId) {
        return new ShipmentIdDto(shipmentId.value());
    }

    @Override
    public boolean equals(final Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
