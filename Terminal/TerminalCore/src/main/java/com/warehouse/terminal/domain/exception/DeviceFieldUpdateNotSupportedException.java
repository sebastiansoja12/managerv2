package com.warehouse.terminal.domain.exception;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.exception.ProblemDetailsException;
import com.warehouse.commonassets.identificator.DeviceId;

public class DeviceFieldUpdateNotSupportedException extends ProblemDetailsException {

    private static final String TYPE = "https://warehouse.dev/problems/device-field-update-not-supported";
    private static final String TITLE = "Unsupported Device Field Update";

    private DeviceFieldUpdateNotSupportedException(final String detail) {
        super(TYPE, TITLE, 400, detail);
    }

    public static DeviceFieldUpdateNotSupportedException forField(final DeviceId deviceId,
                                                                  final DeviceType deviceType,
                                                                  final String fieldName) {
        final String detail = "Field '" + fieldName + "' is not supported for device type "
                + deviceType + " (deviceId=" + (deviceId != null ? deviceId.value() : null) + ")";
        return new DeviceFieldUpdateNotSupportedException(detail);
    }
}
