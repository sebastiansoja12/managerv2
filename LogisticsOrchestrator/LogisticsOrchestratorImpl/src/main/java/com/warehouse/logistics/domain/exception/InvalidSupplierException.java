package com.warehouse.logistics.domain.exception;

public class InvalidSupplierException extends RuntimeException {

    public InvalidSupplierException(final String message) {
        super(message);
    }
}
