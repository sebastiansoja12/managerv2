package com.warehouse.shipment.infrastructure.adapter.primary.exception;

import com.warehouse.exceptionhandler.exception.RestException;
import org.springframework.http.HttpStatus;

import java.util.List;

public class SignatureValidationException extends RestException {

    private final List<String> validationErrors;

    public SignatureValidationException(final List<String> errors, final HttpStatus httpStatus) {
        super(httpStatus.value(), String.join(", ", errors));
        this.validationErrors = errors;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
}
