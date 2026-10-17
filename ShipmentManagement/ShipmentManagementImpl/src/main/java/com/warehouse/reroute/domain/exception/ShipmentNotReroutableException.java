package com.warehouse.reroute.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class ShipmentNotReroutableException extends RestException {

    public ShipmentNotReroutableException() {
        super(409, "Shipment data can no longer be changed");
    }
}
