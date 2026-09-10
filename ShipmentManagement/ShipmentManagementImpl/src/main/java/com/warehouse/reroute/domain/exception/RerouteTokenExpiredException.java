package com.warehouse.reroute.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class RerouteTokenExpiredException extends RestException {

    public RerouteTokenExpiredException() {
        super(410, "Reroute token expired or was already used. Request a new token");
    }
}
