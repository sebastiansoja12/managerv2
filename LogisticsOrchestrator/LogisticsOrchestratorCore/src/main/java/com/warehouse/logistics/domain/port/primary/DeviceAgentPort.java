package com.warehouse.logistics.domain.port.primary;

import com.warehouse.terminal.DeviceInformation;

public interface DeviceAgentPort {
    void updateDeviceIfNeed(final DeviceInformation deviceInformation);
}
