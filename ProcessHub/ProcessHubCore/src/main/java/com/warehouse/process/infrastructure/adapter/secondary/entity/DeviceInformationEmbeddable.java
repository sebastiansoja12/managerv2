package com.warehouse.process.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.enumeration.DeviceUserType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;

import jakarta.persistence.*;

@Embeddable
public class DeviceInformationEmbeddable {

    @AttributeOverride(name = "value", column = @Column(name = "device_id", nullable = false))
    private DeviceId deviceId;

    @AttributeOverride(name = "value", column = @Column(name = "department_code", nullable = false))
    private DepartmentCode departmentCode;

    @AttributeOverride(name = "value", column = @Column(name = "user_id", nullable = false))
    private UserId userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false)
    private DeviceType deviceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_user_type", nullable = false)
    private DeviceUserType deviceUserType;

    @Column(name = "version", nullable = false)
    private String version;

    public DeviceInformationEmbeddable() {

    }

	public DeviceInformationEmbeddable(final DepartmentCode departmentCode, final DeviceId deviceId,
			final DeviceType deviceType, final DeviceUserType deviceUserType, final UserId userId,
			final String version) {
        this.departmentCode = departmentCode;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.deviceUserType = deviceUserType;
        this.userId = userId;
        this.version = version;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public DeviceUserType getDeviceUserType() {
        return deviceUserType;
    }

    public UserId getUserId() {
        return userId;
    }

    public String getVersion() {
        return version;
    }
}

