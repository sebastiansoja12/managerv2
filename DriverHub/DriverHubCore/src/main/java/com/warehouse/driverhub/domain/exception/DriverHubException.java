package com.warehouse.driverhub.domain.exception;

import com.warehouse.commonassets.exception.ProblemDetailsException;

import java.util.Map;

public class DriverHubException extends ProblemDetailsException {

    private final String code;

    public DriverHubException(final int status, final String code, final String message) {
        super("about:blank", "Carrier request rejected", status, message, null, Map.of("code", code));
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static DriverHubException notFound(final String message) {
        return new DriverHubException(404, "CARRIER_NOT_FOUND", message);
    }

    public static DriverHubException invalid(final String message) {
        return new DriverHubException(400, "CARRIER_INVALID", message);
    }
}
