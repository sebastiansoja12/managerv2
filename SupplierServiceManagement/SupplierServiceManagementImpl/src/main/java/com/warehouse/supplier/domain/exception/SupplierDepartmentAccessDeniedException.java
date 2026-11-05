package com.warehouse.supplier.domain.exception;

public class SupplierDepartmentAccessDeniedException extends RuntimeException {

    public SupplierDepartmentAccessDeniedException(final String message) {
        super(message);
    }
}
