package com.warehouse.logistics.domain.port.secondary;

import com.warehouse.terminal.DeviceInformation;

public interface DeviceAgentServicePort {
    void validateDevice(final DeviceInformation deviceInformation);
    void updateDevice(final DeviceInformation deviceInformation);
}
