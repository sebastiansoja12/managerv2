package com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.exception;


import com.warehouse.softwareconfiguration.infrastructure.adapter.primary.exception.RestException;

public class SoftwarePropertyNotFoundException extends RestException {
    public SoftwarePropertyNotFoundException(int code, String exMessage) {
        super(code, exMessage);
    }
}
