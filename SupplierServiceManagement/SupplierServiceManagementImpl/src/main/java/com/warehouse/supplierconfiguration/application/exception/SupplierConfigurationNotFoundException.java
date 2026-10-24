package com.warehouse.supplierconfiguration.application.exception;

public class SupplierConfigurationNotFoundException extends RuntimeException {

    public SupplierConfigurationNotFoundException(final String message) {
        super(message);
    }
}
