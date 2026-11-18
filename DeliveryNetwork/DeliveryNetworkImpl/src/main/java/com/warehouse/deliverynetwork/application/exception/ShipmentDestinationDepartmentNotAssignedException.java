package com.warehouse.deliverynetwork.application.exception;

import com.warehouse.commonassets.identificator.TrackingNumber;

public class ShipmentDestinationDepartmentNotAssignedException extends RuntimeException {

    public ShipmentDestinationDepartmentNotAssignedException(final TrackingNumber trackingNumber) {
        super("Shipment " + trackingNumber.value() + " has no destination department assigned yet");
    }
}
