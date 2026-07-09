package com.warehouse.terminal.infrastructure.adapter.primary.exception;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.warehouse.commonassets.exception.ProblemDetailsException;
import com.warehouse.terminal.infrastructure.adapter.primary.validation.DeviceInvalidParam;

public class DeviceInvalidUpdateRequestException extends ProblemDetailsException {

    private static final String TYPE = "https://warehouse.dev/problems/device-update-request-validation";
    private static final String TITLE = "Invalid Device Update Request";
    private static final String DETAIL = "Invalid device update request payload";

    private DeviceInvalidUpdateRequestException(final List<DeviceInvalidParam> invalidParams) {
        super(TYPE, TITLE, 400, DETAIL, null, buildProperties(invalidParams));
    }

    public static DeviceInvalidUpdateRequestException of(final List<DeviceInvalidParam> invalidParams) {
        return new DeviceInvalidUpdateRequestException(invalidParams);
    }

    private static Map<String, Object> buildProperties(final List<DeviceInvalidParam> invalidParams) {
        final Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("errors", invalidParams);
        return properties;
    }
}
