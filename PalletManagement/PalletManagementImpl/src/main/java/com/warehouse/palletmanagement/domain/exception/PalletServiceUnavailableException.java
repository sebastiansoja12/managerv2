package com.warehouse.palletmanagement.domain.exception;

import com.warehouse.commonassets.exception.ProblemDetailsException;

import java.util.Map;

public class PalletServiceUnavailableException extends ProblemDetailsException {

    public static final String CODE = "PALLET_SERVICE_UNAVAILABLE";

    public PalletServiceUnavailableException(final String message) {
        super("about:blank", "Pallet service unavailable", 503, message, null, Map.of("code", CODE));
    }

    public String getCode() {
        return CODE;
    }
}
