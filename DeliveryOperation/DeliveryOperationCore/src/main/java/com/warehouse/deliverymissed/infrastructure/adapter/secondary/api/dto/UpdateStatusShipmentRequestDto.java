package com.warehouse.deliverymissed.infrastructure.adapter.secondary.api.dto;


public class UpdateStatusShipmentRequestDto {
    private ShipmentIdDto shipmentId;
    private ShipmentStatusDto shipmentStatus;

    public UpdateStatusShipmentRequestDto() {
    }

    public UpdateStatusShipmentRequestDto(final ShipmentIdDto shipmentId, final ShipmentStatusDto shipmentStatus) {
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
