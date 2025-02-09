package com.warehouse.pallet.domain.model;

import com.warehouse.pallet.configuration.identificator.DepartmentCode;
import com.warehouse.pallet.domain.enumeration.DeviceType;
import com.warehouse.pallet.domain.enumeration.DeviceUserType;
import com.warehouse.pallet.domain.vo.DeviceId;
import com.warehouse.pallet.domain.vo.UserId;

public class DeviceInformation {
    private DeviceId deviceId;
    private DepartmentCode departmentCode;
    private UserId userId;
    private DeviceType deviceType;
    private DeviceUserType deviceUserType;
    private String version;

    public DeviceInformation(final DeviceId deviceId,
                             final DepartmentCode departmentCode,
                             final UserId userId,
                             final DeviceType deviceType,
                             final DeviceUserType deviceUserType,
                             final String version) {
        this.deviceId = deviceId;
        this.departmentCode = departmentCode;
        this.userId = userId;
        this.deviceType = deviceType;
        this.deviceUserType = deviceUserType;
        this.version = version;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(final DeviceId deviceId) {
        this.deviceId = deviceId;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(final UserId userId) {
        this.userId = userId;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(final DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public DeviceUserType getDeviceUserType() {
        return deviceUserType;
    }

    public void setDeviceUserType(final DeviceUserType deviceUserType) {
        this.deviceUserType = deviceUserType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }
}
