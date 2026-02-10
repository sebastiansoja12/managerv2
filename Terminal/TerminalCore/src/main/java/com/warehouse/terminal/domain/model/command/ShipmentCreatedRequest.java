package com.warehouse.terminal.domain.model.command;

import com.warehouse.commonassets.identificator.ShipmentId;

public class ShipmentCreatedRequest {
    private final ShipmentId shipmentId;

    private final ShipmentId shipmentRelatedId;

    public ShipmentCreatedRequest(ShipmentId shipmentId, ShipmentId shipmentRelatedId) {
        this.shipmentId = shipmentId;
        this.shipmentRelatedId = shipmentRelatedId;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public ShipmentId getShipmentRelatedId() {
        return shipmentRelatedId;
    }
}
