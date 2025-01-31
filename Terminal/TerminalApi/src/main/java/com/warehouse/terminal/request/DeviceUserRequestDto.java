package com.warehouse.terminal.request;

import com.warehouse.terminal.dto.DeviceIdDto;
import com.warehouse.terminal.dto.UsernameDto;

public class DeviceUserRequestDto {
    private DeviceIdDto deviceId;
    private UsernameDto username;

    public DeviceUserRequestDto() {
    }

    public DeviceUserRequestDto(final DeviceIdDto deviceId, final UsernameDto username) {
        this.deviceId = deviceId;
        this.username = username;
    }

    public DeviceIdDto getDeviceId() {
        return deviceId;
    }

    public UsernameDto getUserId() {
        return username;
    }
}
