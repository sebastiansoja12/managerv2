package com.warehouse.tracking.domain.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import com.warehouse.commonassets.exception.ProblemDetailsException;

public class TrackingException extends ProblemDetailsException {

    public TrackingException(final TrackingErrorCode errorCode,
                             final int status,
                             final String detail,
                             final String requestId) {
        super("https://warehouse.dev/problems/tracking/" + errorCode.name().toLowerCase().replace('_', '-'),
                "Tracking request failed", status, detail, null, properties(errorCode, requestId));
    }

    private static Map<String, Object> properties(final TrackingErrorCode errorCode, final String requestId) {
        final Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("code", errorCode.name());
        if (requestId != null) {
            properties.put("requestId", requestId);
        }
        return properties;
    }
}
