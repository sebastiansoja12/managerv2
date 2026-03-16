package com.warehouse.terminal.request;

import com.warehouse.terminal.dto.DeviceIdDto;
import com.warehouse.terminal.dto.VersionDto;

public class DeviceVersionRequestDto {
    private DeviceIdDto deviceId;
    private VersionDto version;

    public DeviceVersionRequestDto() {
    }

    public DeviceVersionRequestDto(final DeviceIdDto deviceId, final VersionDto version) {
        this.deviceId = deviceId;
        this.version = version;
    }

    public DeviceIdDto getDeviceId() {
        return deviceId;
    }

    public VersionDto getVersion() {
        return version;
    }
}
