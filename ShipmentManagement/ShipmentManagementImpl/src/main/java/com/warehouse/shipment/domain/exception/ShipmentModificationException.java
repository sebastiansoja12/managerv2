package com.warehouse.shipment.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class ShipmentModificationException extends RestException {

    public ShipmentModificationException(final String message) {
        super(400, message);
    }
}
