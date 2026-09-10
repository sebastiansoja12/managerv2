package com.warehouse.logistics.application.port.primary;

import com.warehouse.terminal.DeviceInformation;

public interface DeviceAgentPort {
    void updateDeviceIfNeed(final DeviceInformation deviceInformation);
}
