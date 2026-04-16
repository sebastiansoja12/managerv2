package com.warehouse.terminal.domain.vo;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.dto.DeviceUpdateRequestDto;

public record DeviceVersionRequest(DeviceId deviceId, String version) {
    public static DeviceVersionRequest from(final DeviceUpdateRequestDto deviceUpdateRequest) {
        final DeviceId deviceId = new DeviceId(deviceUpdateRequest.deviceId().value());
        final String version = deviceUpdateRequest.version().value();
        return new DeviceVersionRequest(deviceId, version);
    }
}
