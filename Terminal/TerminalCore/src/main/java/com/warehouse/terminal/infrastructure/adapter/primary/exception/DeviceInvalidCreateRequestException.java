package com.warehouse.terminal.infrastructure.adapter.primary.exception;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.warehouse.commonassets.exception.ProblemDetailsException;
import com.warehouse.terminal.infrastructure.adapter.primary.validation.DeviceInvalidParam;

public class DeviceInvalidCreateRequestException extends ProblemDetailsException {

    private static final String TYPE = "https://warehouse.dev/problems/device-create-request-validation";
    private static final String TITLE = "Invalid Device Create Request";
    private static final String DETAIL = "Invalid device create request payload";

    private DeviceInvalidCreateRequestException(final List<DeviceInvalidParam> invalidParams) {
        super(TYPE, TITLE, 400, DETAIL, null, buildProperties(invalidParams));
    }

    public static DeviceInvalidCreateRequestException of(final List<DeviceInvalidParam> invalidParams) {
        return new DeviceInvalidCreateRequestException(invalidParams);
    }

    private static Map<String, Object> buildProperties(final List<DeviceInvalidParam> invalidParams) {
        final Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("errors", invalidParams);
        return properties;
    }
}
