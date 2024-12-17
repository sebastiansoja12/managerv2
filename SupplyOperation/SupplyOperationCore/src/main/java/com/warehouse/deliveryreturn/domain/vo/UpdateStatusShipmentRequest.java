package com.warehouse.deliveryreturn.domain.vo;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;

public class UpdateStatusShipmentRequest {

    private final ShipmentId shipmentId;
    private final ShipmentStatus shipmentStatus = ShipmentStatus.RETURN;

    public UpdateStatusShipmentRequest(final Long shipmentId) {
        this.shipmentId = new ShipmentId(shipmentId);
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public ShipmentStatus getShipmentStatus() {
        return shipmentStatus;
    }
}
