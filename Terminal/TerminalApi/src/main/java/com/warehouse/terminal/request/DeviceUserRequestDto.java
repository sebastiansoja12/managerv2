package com.warehouse.terminal.request;

import com.warehouse.terminal.dto.DeviceIdDto;
import com.warehouse.terminal.dto.UserIdDto;

public class DeviceUserRequestDto {
    private DeviceIdDto deviceId;
    private UserIdDto userId;

    public DeviceUserRequestDto() {
    }

    public DeviceUserRequestDto(final DeviceIdDto deviceId, final UserIdDto userId) {
        this.deviceId = deviceId;
        this.userId = userId;
    }

    public DeviceIdDto getDeviceId() {
        return deviceId;
    }

    public UserIdDto getUserId() {
        return userId;
    }
}
