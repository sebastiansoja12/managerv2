package com.warehouse.pickuppoint.domain.exception;

public class PickupPointUnavailableException extends RuntimeException {

    public PickupPointUnavailableException(final String message) {
        super(message);
    }
}
