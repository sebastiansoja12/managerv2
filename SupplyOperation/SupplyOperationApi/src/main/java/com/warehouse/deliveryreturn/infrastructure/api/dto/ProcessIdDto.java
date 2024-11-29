package com.warehouse.deliveryreturn.infrastructure.api.dto;

import java.util.UUID;

public class ProcessIdDto {
    private final UUID processId;
    private final ShipmentIdDto shipmentId;

    public ProcessIdDto(final UUID processId, final ShipmentIdDto shipmentId) {
        this.processId = processId;
        this.shipmentId = shipmentId;
    }

    public UUID getProcessId() {
        return processId;
    }

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }
}
