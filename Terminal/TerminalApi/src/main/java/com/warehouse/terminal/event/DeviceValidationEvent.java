package com.warehouse.terminal.event;

import com.warehouse.terminal.dto.DeviceValidationRequestDto;
import lombok.Builder;

import java.time.Instant;

public class DeviceValidationEvent extends DeviceEvent implements DeviceLogEvent {

    private final DeviceValidationRequestDto deviceValidationRequest;

    @Builder
    public DeviceValidationEvent(
            final DeviceValidationRequestDto deviceValidationRequest, final Instant timestamp) {
        super(timestamp);
        this.deviceValidationRequest = deviceValidationRequest;
    }

    public DeviceValidationRequestDto getDeviceValidationRequest() {
        return deviceValidationRequest;
    }
}
