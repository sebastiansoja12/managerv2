package com.warehouse.deliverynetwork.application.exception;

public class MissingOperatorContextException extends RuntimeException {

    public MissingOperatorContextException() {
        super("Current operator context is required for delivery network operations");
    }
}
