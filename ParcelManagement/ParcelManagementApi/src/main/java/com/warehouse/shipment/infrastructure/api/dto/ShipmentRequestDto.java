package com.warehouse.shipment.infrastructure.api.dto;

import java.io.Serializable;
import java.util.Objects;

public class ShipmentRequestDto implements Serializable {

    private ShipmentDto shipment;

    public ShipmentRequestDto(final ShipmentDto shipment) {
        this.shipment = shipment;
    }

    public ShipmentRequestDto() {
    }

    public ShipmentDto getShipment() {
        return shipment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ShipmentRequestDto that = (ShipmentRequestDto) o;
        return Objects.equals(shipment, that.shipment);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(shipment);
    }
}
