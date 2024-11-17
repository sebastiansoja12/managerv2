package com.warehouse.delivery.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;

public class DeviceInformation {
    private final DeviceId deviceId;
    private final DepartmentCode departmentCode;
    private final String version;
    private final String username;

    public DeviceInformation(final DeviceId deviceId,
                             final DepartmentCode departmentCode,
                             final String version,
                             final String username) {
        this.deviceId = deviceId;
        this.departmentCode = departmentCode;
        this.version = version;
        this.username = username;
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
