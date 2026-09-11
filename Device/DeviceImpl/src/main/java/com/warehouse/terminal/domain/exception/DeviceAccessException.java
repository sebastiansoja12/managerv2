package com.warehouse.terminal.domain.exception;

import com.warehouse.commonassets.exception.ProblemDetailsException;

public class DeviceAccessException extends ProblemDetailsException {

    private static final String TYPE = "https://warehouse.dev/problems/device-access";
    private static final String TITLE = "Device Access Denied";

    private DeviceAccessException(final int status, final String detail) {
        super(TYPE, TITLE, status, detail);
    }

    public static DeviceAccessException invalidActivationCode() {
        return new DeviceAccessException(401, "Activation code is invalid or expired");
    }

    public static DeviceAccessException missingInstallationId() {
        return new DeviceAccessException(400, "Installation identifier is required to pair a new device");
    }

    public static DeviceAccessException invalidCredentials() {
        return new DeviceAccessException(401, "Invalid device credentials");
    }

    public static DeviceAccessException deviceNotActive() {
        return new DeviceAccessException(403, "Device is not active");
    }

    public static DeviceAccessException deviceNotAssignedToUser() {
        return new DeviceAccessException(403, "Device does not belong to user");
    }

    public static DeviceAccessException userOutsideDeviceDepartment() {
        return new DeviceAccessException(403, "User does not belong to device department");
    }
}
