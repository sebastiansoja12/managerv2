package com.warehouse.terminal.domain.vo;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;

public class DeviceTypeRequest {
    private final DeviceId deviceId;
    private final DeviceType deviceType;

    public DeviceTypeRequest(final DeviceId deviceId, final DeviceType deviceType) {
        this.deviceId = deviceId;
        this.deviceType = deviceType;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }
}
