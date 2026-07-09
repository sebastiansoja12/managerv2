package com.warehouse.terminal.request;

import com.warehouse.terminal.dto.DeviceIdDto;
import com.warehouse.terminal.dto.DeviceTypeDto;

public class DeviceTypeRequestDto {
    private DeviceIdDto deviceId;
    private DeviceTypeDto deviceType;

    public DeviceTypeRequestDto() {
    }

    public DeviceTypeRequestDto(final DeviceIdDto deviceId, final DeviceTypeDto deviceType) {
        this.deviceId = deviceId;
        this.deviceType = deviceType;
    }

    public DeviceIdDto getDeviceId() {
        return deviceId;
    }

    public DeviceTypeDto getDeviceType() {
        return deviceType;
    }
}
