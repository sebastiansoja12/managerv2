package com.warehouse.delivery.domain.vo;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.delivery.domain.enumeration.DeviceUserType;

public class DeviceInformation {
    private final DeviceId deviceId;
    private final DepartmentCode departmentCode;
    private final DeviceType deviceType;
    private final DeviceUserType deviceUserType;
    private final String version;
    private final String username;

    public DeviceInformation(final DeviceId deviceId,
                             final DepartmentCode departmentCode,
                             final DeviceType deviceType,
                             final DeviceUserType deviceUserType,
                             final String version,
                             final String username) {
        this.deviceId = deviceId;
        this.departmentCode = departmentCode;
        this.deviceType = deviceType;
        this.deviceUserType = deviceUserType;
        this.version = version;
        this.username = username;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public DeviceUserType getDeviceUserType() {
        return deviceUserType;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public String getVersion() {
        return version;
    }

    public String getUsername() {
        return username;
    }
}
