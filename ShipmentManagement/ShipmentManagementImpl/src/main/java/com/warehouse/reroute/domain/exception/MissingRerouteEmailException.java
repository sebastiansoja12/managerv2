package com.warehouse.reroute.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class MissingRerouteEmailException extends RestException {

    public MissingRerouteEmailException() {
        super(422, "Shipment has no e-mail address to send the reroute token to");
    }
}
