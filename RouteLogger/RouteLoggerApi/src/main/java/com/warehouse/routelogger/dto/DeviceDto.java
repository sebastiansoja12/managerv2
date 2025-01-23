package com.warehouse.routelogger.dto;

public class DeviceDto {
    private final DeviceIdDto deviceId;
    private final DepartmentCodeDto departmentCode;
    private final UserIdDto userId;
    private final DeviceVersionDto deviceVersion;
    private final DeviceTypeDto deviceType;

    public DeviceDto(final DeviceIdDto deviceId,
                     final DepartmentCodeDto departmentCode,
                     final UserIdDto userId,
                     final DeviceVersionDto deviceVersion,
                     final DeviceTypeDto deviceType) {
        this.deviceId = deviceId;
        this.departmentCode = departmentCode;
        this.userId = userId;
        this.deviceVersion = deviceVersion;
        this.deviceType = deviceType;
    }

    public DeviceIdDto getDeviceId() {
        return deviceId;
    }

    public DepartmentCodeDto getDepartmentCode() {
        return departmentCode;
    }

    public UserIdDto getUserId() {
        return userId;
    }

    public DeviceVersionDto getDeviceVersion() {
        return deviceVersion;
    }

    public DeviceTypeDto getDeviceType() {
        return deviceType;
    }
}
