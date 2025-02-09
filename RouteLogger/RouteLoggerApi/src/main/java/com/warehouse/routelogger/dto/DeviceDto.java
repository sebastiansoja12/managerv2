package com.warehouse.routelogger.dto;

public class DeviceDto {
    private final DeviceIdDto deviceId;
    private final DepartmentCodeDto departmentCode;
    private final UsernameDto username;
    private final DeviceVersionDto deviceVersion;
    private final DeviceTypeDto deviceType;

    public DeviceDto(final DeviceIdDto deviceId,
                     final DepartmentCodeDto departmentCode,
                     final UsernameDto username,
                     final DeviceVersionDto deviceVersion,
                     final DeviceTypeDto deviceType) {
        this.deviceId = deviceId;
        this.departmentCode = departmentCode;
        this.username = username;
        this.deviceVersion = deviceVersion;
        this.deviceType = deviceType;
    }

    public DeviceIdDto getDeviceId() {
        return deviceId;
    }

    public DepartmentCodeDto getDepartmentCode() {
        return departmentCode;
    }

    public UsernameDto getUsername() {
        return username;
    }

    public DeviceVersionDto getDeviceVersion() {
        return deviceVersion;
    }

    public DeviceTypeDto getDeviceType() {
        return deviceType;
    }
}
