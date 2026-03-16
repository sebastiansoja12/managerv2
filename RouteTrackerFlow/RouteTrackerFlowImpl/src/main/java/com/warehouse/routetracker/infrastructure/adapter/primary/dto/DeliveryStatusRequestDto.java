package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

public class DeliveryStatusRequestDto {
    private ShipmentIdDto shipmentId;
    private ProcessTypeDto processType;

    public DeliveryStatusRequestDto() {
    }

    public DeliveryStatusRequestDto(final ShipmentIdDto shipmentId, final ProcessTypeDto processType) {
        this.shipmentId = shipmentId;
        this.processType = processType;
    }

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }

    public ProcessTypeDto getProcessType() {
        return processType;
    }
}
