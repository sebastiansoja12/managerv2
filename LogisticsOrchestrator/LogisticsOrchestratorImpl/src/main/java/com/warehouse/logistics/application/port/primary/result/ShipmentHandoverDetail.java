package com.warehouse.logistics.application.port.primary.result;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public record ShipmentHandoverDetail(TrackingNumber trackingNumber,
                                     ShipmentId shipmentId,
                                     boolean handedOver,
                                     String errorMessage) {

    public ShipmentHandoverDetail failed(final String failureMessage) {
        return new ShipmentHandoverDetail(trackingNumber, shipmentId, false, failureMessage);
    }
}
