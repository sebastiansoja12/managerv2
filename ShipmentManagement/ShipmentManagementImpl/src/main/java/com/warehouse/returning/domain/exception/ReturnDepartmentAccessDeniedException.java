package com.warehouse.returning.domain.exception;

public class ReturnDepartmentAccessDeniedException extends RuntimeException {

    public ReturnDepartmentAccessDeniedException(final String message) {
        super(message);
    }
}
