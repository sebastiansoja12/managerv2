package com.warehouse.softwareconfiguration.configuration.exception;

import com.warehouse.softwareconfiguration.infrastructure.adapter.primary.exception.RestException;

public class ApplicationAccessException extends RestException {
    public ApplicationAccessException(int code, String exMessage) {
        super(code, exMessage);
    }
}
