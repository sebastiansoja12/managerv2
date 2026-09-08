package com.warehouse.logistics.application.port.secondary;

import com.warehouse.terminal.DeviceInformation;

public interface DeviceAgentServicePort {
    void validateDevice(final DeviceValidateCommand command);
    void updateDevice(final DeviceInformation deviceInformation);
}
