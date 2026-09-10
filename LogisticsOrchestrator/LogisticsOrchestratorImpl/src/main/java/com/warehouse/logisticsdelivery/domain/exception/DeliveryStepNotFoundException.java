package com.warehouse.logisticsdelivery.domain.exception;

public class DeliveryStepNotFoundException extends RuntimeException {

    public DeliveryStepNotFoundException(final String message) {
        super(message);
    }
}
