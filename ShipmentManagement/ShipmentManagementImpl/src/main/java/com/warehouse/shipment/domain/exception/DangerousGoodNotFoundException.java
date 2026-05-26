package com.warehouse.shipment.domain.exception;

public class DangerousGoodNotFoundException extends RuntimeException {

    public DangerousGoodNotFoundException(final String message) {
        super(message);
    }
}
