package com.warehouse.reroute.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class RerouteTokenRequestsExceededException extends RestException {

    public RerouteTokenRequestsExceededException() {
        super(429, "Too many tokens requested. Try again later");
    }
}
