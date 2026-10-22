package com.warehouse.supplierconfiguration.application.exception;

public class SupplierConfigurationAccessDeniedException extends RuntimeException {

    public SupplierConfigurationAccessDeniedException(final String message) {
        super(message);
    }
}
