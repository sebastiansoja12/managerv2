package com.warehouse.reroute.domain.exception;

import com.warehouse.exception.RestException;
import com.warehouse.reroute.domain.exception.enumeration.RerouteExceptionCodes;

public class RerouteException extends RestException {
    public RerouteException(RerouteExceptionCodes code) {
        super(code.getCode(), code.getMessage());
    }
}
