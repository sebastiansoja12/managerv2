package com.warehouse.terminal.domain.exception;

import org.springframework.web.client.RestClientException;

public class DepartmentNotFoundException extends RestClientException {

    private static final String message = "Department %s not found";

    public DepartmentNotFoundException(final Object value) {
        super(String.format(message, value));
    }
}
