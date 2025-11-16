package com.warehouse.department.domain.exception;

public class WrongUserAssignedException extends RestException {
    public WrongUserAssignedException(final String message) {
        super(message);
    }
}
