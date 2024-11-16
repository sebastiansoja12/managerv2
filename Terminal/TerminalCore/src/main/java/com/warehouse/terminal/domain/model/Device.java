package com.warehouse.terminal.domain.model;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.enumeration.ExecutionSourceType;

public class Device {
    private DeviceId deviceId;
    private String version;
    private DeviceType deviceType;
    private UserId userId;

	public Device(final DeviceId deviceId,
                  final String version,
                  final DeviceType deviceType,
                  final UserId userId) {
        this.deviceId = deviceId;
        this.version = version;
        this.deviceType = deviceType;
        this.userId = userId;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(final DeviceId deviceId) {
        this.deviceId = deviceId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(final DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getType() {
        return ExecutionSourceType.DEVICE.name();
    }

    public UserId getUserId() {
        return userId;
    }
}
