package com.warehouse.reroute.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.reroute.domain.exception.enumeration.RerouteExceptionCodes;

public class RerouteTokenExpiredException extends RestException {

    public RerouteTokenExpiredException(RerouteExceptionCodes code) {
        super(code.getCode(), code.getMessage());
    }
}
