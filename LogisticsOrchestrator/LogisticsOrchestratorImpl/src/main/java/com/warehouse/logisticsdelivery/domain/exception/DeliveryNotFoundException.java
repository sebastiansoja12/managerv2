package com.warehouse.logisticsdelivery.domain.exception;

public class DeliveryNotFoundException extends RuntimeException {

    public DeliveryNotFoundException(final String message) {
        super(message);
    }
}
