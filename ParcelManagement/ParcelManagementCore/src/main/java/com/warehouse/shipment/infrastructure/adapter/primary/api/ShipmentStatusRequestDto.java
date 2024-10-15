package com.warehouse.shipment.infrastructure.adapter.primary.api;

import com.warehouse.shipment.infrastructure.api.dto.ShipmentIdDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentStatusDto;

public class ShipmentStatusRequestDto {
    private ShipmentIdDto shipmentId;
    private ShipmentStatusDto shipmentStatus;

    public ShipmentStatusRequestDto(final ShipmentIdDto shipmentId, final ShipmentStatusDto shipmentStatus) {
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
