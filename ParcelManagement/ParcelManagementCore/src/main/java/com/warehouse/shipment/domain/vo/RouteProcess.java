package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;

import java.util.UUID;


public class RouteProcess {
    private final ShipmentId shipmentId;
    private final UUID processId;

    public RouteProcess(final ShipmentId shipmentId, final UUID processId) {
        this.shipmentId = shipmentId;
        this.processId = processId;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public UUID getProcessId() {
        return processId;
    }

    public static RouteProcess from(final ShipmentId shipmentId, final UUID processId) {
        return new RouteProcess(shipmentId, processId);
    }
}
