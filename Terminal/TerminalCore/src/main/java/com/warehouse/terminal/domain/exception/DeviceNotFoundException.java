package com.warehouse.terminal.domain.exception;

import com.warehouse.commonassets.exception.ProblemDetailsException;
import com.warehouse.commonassets.identificator.DeviceId;

public class DeviceNotFoundException extends ProblemDetailsException {

    private static final String TYPE = "https://warehouse.dev/problems/device-not-found";
    private static final String TITLE = "Device Not Found";

    private DeviceNotFoundException(final String detail) {
        super(TYPE, TITLE, 404, detail);
    }

    public static DeviceNotFoundException forDeviceId(final DeviceId deviceId) {
        final String detail = "Device with id "
                + (deviceId != null ? deviceId.value() : null)
                + " was not found";
        return new DeviceNotFoundException(detail);
    }
}
