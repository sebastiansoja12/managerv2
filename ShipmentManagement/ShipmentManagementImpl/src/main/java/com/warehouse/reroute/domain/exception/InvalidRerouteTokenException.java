package com.warehouse.reroute.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class InvalidRerouteTokenException extends RestException {

    public InvalidRerouteTokenException() {
        super(403, "Invalid reroute token");
    }
}
