package com.warehouse.shipment.domain.vo;


import com.warehouse.commonassets.identificator.ShipmentId;

import java.util.UUID;

public record ShipmentCreateResponse(UUID routeProcessId, ShipmentId shipmentId) {
    public static ShipmentCreateResponse from(final RouteProcess routeProcess) {
        return new ShipmentCreateResponse(routeProcess.getProcessId(), routeProcess.getShipmentId());
    }
}
