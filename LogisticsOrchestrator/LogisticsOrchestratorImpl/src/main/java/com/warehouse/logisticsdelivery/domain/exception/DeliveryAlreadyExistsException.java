package com.warehouse.logisticsdelivery.domain.exception;

public class DeliveryAlreadyExistsException extends RuntimeException {

    public DeliveryAlreadyExistsException(final String message) {
        super(message);
    }
}
