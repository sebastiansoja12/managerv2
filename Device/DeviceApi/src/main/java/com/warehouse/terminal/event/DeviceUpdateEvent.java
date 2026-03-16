package com.warehouse.terminal.event;

import com.warehouse.terminal.dto.DeviceUpdateRequestDto;
import lombok.Builder;

import java.time.Instant;

public class DeviceUpdateEvent extends DeviceEvent implements AgentEvent {

    private final DeviceUpdateRequestDto deviceUpdateRequest;

    @Builder
    public DeviceUpdateEvent(final DeviceUpdateRequestDto deviceUpdateRequest, final Instant timestamp) {
        super(timestamp);
        this.deviceUpdateRequest = deviceUpdateRequest;
    }

    public DeviceUpdateRequestDto getDeviceUpdateRequest() {
        return deviceUpdateRequest;
    }
}
