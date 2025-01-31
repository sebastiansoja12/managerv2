package com.warehouse.logistics.domain.port.secondary;

import com.warehouse.terminal.DeviceInformation;

public interface DeviceValidatorServicePort {
    void validateDevice(final DeviceInformation deviceInformation);
}
