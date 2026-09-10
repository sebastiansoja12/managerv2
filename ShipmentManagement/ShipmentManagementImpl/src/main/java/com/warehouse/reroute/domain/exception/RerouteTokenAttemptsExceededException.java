package com.warehouse.reroute.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class RerouteTokenAttemptsExceededException extends RestException {

    public RerouteTokenAttemptsExceededException() {
        super(429, "Too many invalid attempts. Request a new token");
    }
}
