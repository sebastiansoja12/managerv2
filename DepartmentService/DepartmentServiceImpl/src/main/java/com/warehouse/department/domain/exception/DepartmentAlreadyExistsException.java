package com.warehouse.department.domain.exception;

public class DepartmentAlreadyExistsException extends RestException {
    public DepartmentAlreadyExistsException(final String message) {
        super(message);
    }
}
