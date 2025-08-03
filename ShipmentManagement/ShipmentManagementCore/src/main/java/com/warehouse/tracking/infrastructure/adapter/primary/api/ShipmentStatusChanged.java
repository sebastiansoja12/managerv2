package com.warehouse.tracking.infrastructure.adapter.primary.api;


import java.time.LocalDateTime;

import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentIdDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentStatusDto;


public class ShipmentStatusChanged implements ShipmentStatusEvent {

    private final ShipmentIdDto shipmentId;

    private final ShipmentStatusDto shipmentStatus;

    private final LocalDateTime localDateTime;

    public ShipmentStatusChanged(final ShipmentIdDto shipmentId, final ShipmentStatusDto shipmentStatus) {
        this.shipmentId = shipmentId;
        this.shipmentStatus = shipmentStatus;
        this.localDateTime = LocalDateTime.now();
    }

    public ShipmentStatusDto getShipmentStatus() {
        return shipmentStatus;
    }

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }
}
