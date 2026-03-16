package com.warehouse.routetracker.domain.vo;

import java.util.UUID;

import com.warehouse.commonassets.identificator.ShipmentId;


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
}
