package com.warehouse.billing.pricing.domain.exception;

public class ShipmentNotPriceableException extends RuntimeException {

    public ShipmentNotPriceableException(final String message) {
        super(message);
    }
}
