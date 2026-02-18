package com.warehouse.logistics.domain.port.primary;

import com.warehouse.logistics.domain.model.DeviceValidateCommand;

public interface DeviceValidatorPort {
    void validateDevice(final DeviceValidateCommand command);
}
