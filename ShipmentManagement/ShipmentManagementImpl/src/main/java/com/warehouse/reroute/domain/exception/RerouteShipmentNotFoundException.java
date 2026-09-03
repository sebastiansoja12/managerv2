package com.warehouse.reroute.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class RerouteShipmentNotFoundException extends RestException {

    public RerouteShipmentNotFoundException() {
        super(404, "Shipment was not found");
    }
}
