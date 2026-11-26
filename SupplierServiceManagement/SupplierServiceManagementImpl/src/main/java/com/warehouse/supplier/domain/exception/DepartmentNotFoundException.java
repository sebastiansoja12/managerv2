package com.warehouse.supplier.domain.exception;

public class DepartmentNotFoundException extends BusinessException {

    public DepartmentNotFoundException(final String message) {
        super(message);
    }
}
