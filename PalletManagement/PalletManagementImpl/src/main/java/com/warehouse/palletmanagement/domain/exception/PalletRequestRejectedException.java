package com.warehouse.palletmanagement.domain.exception;

import com.warehouse.commonassets.exception.ProblemDetailsException;

import java.util.Map;

public class PalletRequestRejectedException extends ProblemDetailsException {

    private final String code;

    public PalletRequestRejectedException(final int status, final String code, final String message) {
        super("about:blank", "Pallet request rejected", status, message, null, Map.of("code", code));
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
