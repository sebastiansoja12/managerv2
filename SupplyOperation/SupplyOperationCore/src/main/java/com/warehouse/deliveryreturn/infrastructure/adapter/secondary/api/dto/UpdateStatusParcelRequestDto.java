package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto;


public class UpdateStatusParcelRequestDto {
    private ShipmentIdDto shipmentId;
    private ShipmentStatusDto shipmentStatus;

    public UpdateStatusParcelRequestDto() {
    }

    public UpdateStatusParcelRequestDto(final ShipmentIdDto shipmentId, final ShipmentStatusDto shipmentStatus) {
        this.shipmentId = shipmentId;
        this.shipmentStatus = shipmentStatus;
    }

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }

    public ShipmentStatusDto getShipmentStatus() {
        return shipmentStatus;
    }
}
