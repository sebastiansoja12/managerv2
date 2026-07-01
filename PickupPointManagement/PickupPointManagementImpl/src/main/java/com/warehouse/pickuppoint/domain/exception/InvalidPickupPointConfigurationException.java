package com.warehouse.pickuppoint.domain.exception;

public class InvalidPickupPointConfigurationException extends RuntimeException {

    public InvalidPickupPointConfigurationException(final String message) {
        super(message);
    }
}
