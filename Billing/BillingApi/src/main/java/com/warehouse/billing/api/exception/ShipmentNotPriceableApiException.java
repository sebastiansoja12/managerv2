package com.warehouse.billing.api.exception;

public class ShipmentNotPriceableApiException extends RuntimeException {

    public ShipmentNotPriceableApiException(final String message) {
        super(message);
    }
}
