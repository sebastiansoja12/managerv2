package com.warehouse.shipment.domain.exception;

public class ReturnDetailsUnavailableException extends RuntimeException {
    public ReturnDetailsUnavailableException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
