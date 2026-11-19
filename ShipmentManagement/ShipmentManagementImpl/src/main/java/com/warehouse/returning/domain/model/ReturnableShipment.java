package com.warehouse.returning.domain.model;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public record ReturnableShipment(ShipmentId shipmentId, TrackingNumber trackingNumber, ShipmentStatus status,
                                 ShipmentId relatedShipmentId) {

    public boolean isReturnable() {
        return status == ShipmentStatus.DELIVERY || status == ShipmentStatus.UNDELIVERABLE;
    }

    public void validateReturnRequest() {
        if (!isReturnable()) {
            throw new IllegalArgumentException(
                    "Return can be requested only for a delivered or undeliverable shipment");
        }
    }
}
