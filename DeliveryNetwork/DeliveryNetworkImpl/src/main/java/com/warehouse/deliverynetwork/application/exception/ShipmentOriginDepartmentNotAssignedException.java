package com.warehouse.deliverynetwork.application.exception;

import com.warehouse.commonassets.identificator.TrackingNumber;

public class ShipmentOriginDepartmentNotAssignedException extends RuntimeException {

    public ShipmentOriginDepartmentNotAssignedException(final TrackingNumber trackingNumber) {
        super("Shipment " + trackingNumber.value() + " has not been handed over to an origin department yet");
    }
}
