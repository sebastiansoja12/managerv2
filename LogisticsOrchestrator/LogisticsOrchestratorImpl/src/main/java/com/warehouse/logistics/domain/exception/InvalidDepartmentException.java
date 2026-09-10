package com.warehouse.logistics.domain.exception;

public class InvalidDepartmentException extends RuntimeException {

    public InvalidDepartmentException(final String message) {
        super(message);
    }
}
