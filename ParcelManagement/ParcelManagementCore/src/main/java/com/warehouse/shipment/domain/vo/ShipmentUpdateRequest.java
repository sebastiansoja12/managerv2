package com.warehouse.shipment.domain.vo;

import com.warehouse.shipment.domain.model.ShipmentUpdate;

public class ShipmentUpdateRequest {
    private final ShipmentUpdate shipmentUpdate;

    public ShipmentUpdateRequest(final ShipmentUpdate shipmentUpdate) {
        this.shipmentUpdate = shipmentUpdate;
    }
    public ShipmentUpdate getParcelUpdate() {
        return shipmentUpdate;
    }
}
