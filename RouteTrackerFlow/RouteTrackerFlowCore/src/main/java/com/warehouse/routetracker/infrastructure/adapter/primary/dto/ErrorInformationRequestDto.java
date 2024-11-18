package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

public class ErrorInformationRequestDto {
    private ShipmentIdDto shipmentId;
    private ErrorDto error;

    public ErrorInformationRequestDto() {
    }

    public ErrorInformationRequestDto(final ShipmentIdDto shipmentId, final ErrorDto error) {
        this.shipmentId = shipmentId;
        this.error = error;
    }

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }

    public ErrorDto getError() {
        return error;
    }
}
