package com.warehouse.tracking;


import java.time.LocalDateTime;

import com.warehouse.shipment.infrastructure.api.dto.ShipmentDto;

public class ShipmentStatusChanged implements ShipmentStatusEvent {

    private final ShipmentDto shipment;

    private final LocalDateTime localDateTime;

    public ShipmentStatusChanged(final ShipmentDto shipment) {
        this.shipment = shipment;
        this.localDateTime = LocalDateTime.now();
    }

    public ShipmentDto getShipment() {
        return shipment;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }
}
