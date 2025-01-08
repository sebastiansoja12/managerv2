package com.warehouse.delivery.domain.port.secondary;

import com.warehouse.delivery.domain.vo.DeviceInformation;

public interface DeviceValidatorServicePort {
    void validateDevice(final DeviceInformation deviceInformation);
}
