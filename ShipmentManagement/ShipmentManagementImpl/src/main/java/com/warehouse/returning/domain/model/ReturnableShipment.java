package com.warehouse.returning.domain.model;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;

public record ReturnableShipment(ShipmentId shipmentId, ShipmentStatus status, ShipmentId relatedShipmentId) {

    public void validateReturnRequest() {
        if (status != ShipmentStatus.DELIVERY && status != ShipmentStatus.UNDELIVERABLE) {
            throw new IllegalArgumentException(
                    "Return can be requested only for a delivered or undeliverable shipment");
        }
    }
}
