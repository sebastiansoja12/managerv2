package com.warehouse.organisationstructure.department.domain.exception;

public class WrongUserAssignedException extends RestException {
    public WrongUserAssignedException(final String message) {
        super(message);
    }
}
