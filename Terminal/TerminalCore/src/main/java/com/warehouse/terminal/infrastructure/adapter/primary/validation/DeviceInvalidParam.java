package com.warehouse.terminal.infrastructure.adapter.primary.validation;

public class DeviceInvalidParam {

    private final String name;
    private final String reason;

    private DeviceInvalidParam(final String name, final String reason) {
        this.name = name;
        this.reason = reason;
    }

    public static DeviceInvalidParam missingRequestPayload() {
        return of("request", "request payload must not be null");
    }

    public static DeviceInvalidParam missingDeviceId() {
        return of("deviceId", "is required");
    }

    public static DeviceInvalidParam missingUpdatableFields() {
        return of("request", "must contain at least one updatable field");
    }

    public static DeviceInvalidParam missingUserId() {
        return of("userId", "is required");
    }

    public static DeviceInvalidParam missingDepartmentCode() {
        return of("departmentCode", "is required");
    }

    public static DeviceInvalidParam missingDeviceType() {
        return of("deviceType", "is required");
    }

    public static DeviceInvalidParam scanTypeAllowedOnlyForScanner() {
        return of("scanType", "can be provided only for deviceType=SCANNER");
    }

    public static DeviceInvalidParam scannerTypeAllowedOnlyForScanner() {
        return of("scannerType", "can be provided only for deviceType=SCANNER");
    }

    public static DeviceInvalidParam blankVersion() {
        return of("version", "cannot be blank when provided");
    }

    public static DeviceInvalidParam identityRequired() {
        return of("identity", "is required");
    }

    public static DeviceInvalidParam invalidIdentityIdentifiers() {
        return of("identity", "must contain at least one identifier: hardwareUuid, serialNumber, imei, macAddress, assetTag, barcode, externalSystemId, mdmDeviceId");
    }

    public static DeviceInvalidParam hardwareRequired() {
        return of("hardware", "is required");
    }

    public static DeviceInvalidParam invalidHardware() {
        return of("hardware", "should define at least one of: manufacturer, model, productName");
    }

    public static DeviceInvalidParam softwareRequired() {
        return of("software", "is required");
    }

    public static DeviceInvalidParam softwareUpdateEmpty() {
        return of("software", "must contain at least one field to update");
    }

    public static DeviceInvalidParam networkRequired() {
        return of("network", "is required");
    }

    public static DeviceInvalidParam invalidNetworkType() {
        return of("network.networkType", "is invalid (WIFI|CELLULAR|ETHERNET|OFFLINE|UNKNOWN)");
    }

    public static DeviceInvalidParam securityRequired() {
        return of("security", "is required");
    }

    public static DeviceInvalidParam invalidFailedLoginAttempts() {
        return of("security.failedLoginAttempts", "cannot be negative");
    }

    public static DeviceInvalidParam locationRequired() {
        return of("location", "is required");
    }

    public static DeviceInvalidParam invalidLocationCoordinates() {
        return of("location", "latitude and longitude must be provided together");
    }

    public static DeviceInvalidParam blankExternalDeviceId() {
        return of("externalDeviceId", "cannot be blank when provided");
    }

    public static DeviceInvalidParam invalidStatus() {
        return of("status", "is invalid (ACTIVE|BLOCKED|LOST|RETIRED)");
    }

    public static DeviceInvalidParam ownershipUpdateEmpty() {
        return of("ownership", "must contain at least one field to update");
    }

    public static DeviceInvalidParam ownershipUserIdMissingValue() {
        return of("ownership.userId.value", "is required when userId is provided");
    }

    public static DeviceInvalidParam ownershipPreviousUserIdMissingValue() {
        return of("ownership.previousUserId.value", "is required when previousUserId is provided");
    }

    public static DeviceInvalidParam ownershipBlankDepartmentCode() {
        return of("ownership.departmentCode", "cannot be blank when provided");
    }

    private static DeviceInvalidParam of(final String name, final String reason) {
        return new DeviceInvalidParam(name, reason);
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }
}
