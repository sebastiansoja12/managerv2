package com.warehouse.organisationstructure.department.domain.exception;

public class DepartmentStatusChangeForbiddenException extends RestException {
    public DepartmentStatusChangeForbiddenException(final String message) {
        super(message);
    }
}
