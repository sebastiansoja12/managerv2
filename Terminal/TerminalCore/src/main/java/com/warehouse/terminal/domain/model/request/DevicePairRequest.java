package com.warehouse.terminal.domain.model.request;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;

public class DevicePairRequest {
    private DeviceId deviceId;
    private UserId userId;

    public DevicePairRequest(final DeviceId deviceId, final UserId userId) {
        this.deviceId = deviceId;
        this.userId = userId;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(final DeviceId deviceId) {
        this.deviceId = deviceId;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(final UserId userId) {
        this.userId = userId;
    }
}
