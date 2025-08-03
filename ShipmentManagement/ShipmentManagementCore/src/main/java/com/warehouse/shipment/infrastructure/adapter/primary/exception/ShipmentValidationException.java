package com.warehouse.shipment.infrastructure.adapter.primary.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.warehouse.exceptionhandler.exception.RestException;

public class ShipmentValidationException extends RestException {

    private final List<String> validationErrors;

    public ShipmentValidationException(final List<String> errors, final HttpStatus httpStatus) {
        super(httpStatus.value(), String.join(", ", errors));
        this.validationErrors = errors;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
}
