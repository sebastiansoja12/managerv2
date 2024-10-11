package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.ShipmentUpdate;

public class ShipmentUpdateRequest {
    private final ShipmentId shipmentId;
    private final ShipmentUpdate shipmentUpdate;

    public ShipmentUpdateRequest(final ShipmentId shipmentId, final ShipmentUpdate shipmentUpdate) {
        this.shipmentId = shipmentId;
        this.shipmentUpdate = shipmentUpdate;
    }
    public ShipmentUpdate getShipmentUpdate() {
        return shipmentUpdate;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }
}
