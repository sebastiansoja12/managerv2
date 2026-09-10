package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot.ShipmentId;

public class ShipmentCanceledMessage {

    private final ShipmentId shipmentId;

    @JsonCreator
    public ShipmentCanceledMessage(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }
}
