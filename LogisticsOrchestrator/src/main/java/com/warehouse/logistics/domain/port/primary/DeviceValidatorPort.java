package com.warehouse.logistics.domain.port.primary;

import com.warehouse.terminal.information.Device;

public interface DeviceValidatorPort {
    void validateDevice(final Device device);
}
