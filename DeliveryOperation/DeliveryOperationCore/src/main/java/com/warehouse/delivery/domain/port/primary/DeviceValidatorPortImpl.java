package com.warehouse.delivery.domain.port.primary;

import com.warehouse.delivery.domain.port.secondary.DeviceValidatorServicePort;
import com.warehouse.delivery.domain.vo.DeviceInformation;
import com.warehouse.terminal.information.Device;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeviceValidatorPortImpl implements DeviceValidatorPort {

    private final DeviceValidatorServicePort deviceValidatorServicePort;

    public DeviceValidatorPortImpl(final DeviceValidatorServicePort deviceValidatorServicePort) {
        this.deviceValidatorServicePort = deviceValidatorServicePort;
    }

    @Override
    public void validateDevice(final Device device) {
        final DeviceInformation deviceInformation = DeviceInformation.from(device);
        deviceValidatorServicePort.validateDevice(deviceInformation);
    }
}
