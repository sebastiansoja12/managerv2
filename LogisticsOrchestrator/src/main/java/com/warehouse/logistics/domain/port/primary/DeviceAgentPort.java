package com.warehouse.logistics.domain.port.primary;

import com.warehouse.terminal.information.Device;

public interface DeviceAgentPort {
    void updateDeviceIfNeed(final Device device);
}
