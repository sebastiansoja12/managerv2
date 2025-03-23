package com.warehouse.terminal.event;

import java.time.Instant;

import com.warehouse.terminal.dto.DeviceSettingsRequestDto;

public class DeviceSettingsEvent extends DeviceEvent implements AgentEvent {

    private final DeviceSettingsRequestDto request;

    public DeviceSettingsEvent(final Instant timestamp, final DeviceSettingsRequestDto request) {
        super(timestamp);
        this.request = request;
    }

    public DeviceSettingsRequestDto getRequest() {
        return request;
    }
}
