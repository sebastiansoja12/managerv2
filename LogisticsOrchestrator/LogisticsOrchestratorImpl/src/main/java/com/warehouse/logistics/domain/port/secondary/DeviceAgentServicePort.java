package com.warehouse.logistics.domain.port.secondary;

import com.warehouse.logistics.domain.model.DeviceValidateCommand;
import com.warehouse.terminal.DeviceInformation;

public interface DeviceAgentServicePort {
    void validateDevice(final DeviceValidateCommand command);
    void updateDevice(final DeviceInformation deviceInformation);
}
