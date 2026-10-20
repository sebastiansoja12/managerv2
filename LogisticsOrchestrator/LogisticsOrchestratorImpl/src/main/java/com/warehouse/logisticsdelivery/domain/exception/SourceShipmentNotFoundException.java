package com.warehouse.logisticsdelivery.domain.exception;

public class SourceShipmentNotFoundException extends RuntimeException {

    public SourceShipmentNotFoundException(final String message) {
        super(message);
    }
}
