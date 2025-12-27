package com.warehouse.supplier.domain.exception;

public class SupplierAlreadyExistsException extends BusinessException {
    public SupplierAlreadyExistsException(final String message) {
        super(message);
    }
}
