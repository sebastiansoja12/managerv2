package com.warehouse.shipment.infrastructure.adapter.secondary.exception;

import org.springframework.http.HttpStatusCode;

public class TechnicalException extends RuntimeException {

    private final HttpStatusCode code;

    public TechnicalException(final HttpStatusCode code, final String message) {
        super(message);
        this.code = code;
    }

    public HttpStatusCode getCode() {
        return code;
    }
}
