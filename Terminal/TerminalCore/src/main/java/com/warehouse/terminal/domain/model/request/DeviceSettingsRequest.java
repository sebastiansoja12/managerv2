package com.warehouse.terminal.domain.model.request;

import com.warehouse.commonassets.identificator.DeviceId;

public class DeviceSettingsRequest {
    private DeviceId deviceId;
    private Boolean enableCrossCourierDelivery;

    public DeviceSettingsRequest(final DeviceId deviceId, final Boolean enableCrossCourierDelivery) {
        this.deviceId = deviceId;
        this.enableCrossCourierDelivery = enableCrossCourierDelivery;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(final DeviceId deviceId) {
        this.deviceId = deviceId;
    }

    public Boolean getEnableCrossCourierDelivery() {
        return enableCrossCourierDelivery;
    }

    public void setEnableCrossCourierDelivery(final Boolean enableCrossCourierDelivery) {
        this.enableCrossCourierDelivery = enableCrossCourierDelivery;
    }
}
