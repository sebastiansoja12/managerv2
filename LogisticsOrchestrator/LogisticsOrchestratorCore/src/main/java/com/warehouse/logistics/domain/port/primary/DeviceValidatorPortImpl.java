package com.warehouse.logistics.domain.port.primary;

import com.warehouse.logistics.domain.port.secondary.DeviceAgentServicePort;
import com.warehouse.terminal.DeviceInformation;
import com.warehouse.terminal.information.Device;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeviceValidatorPortImpl implements DeviceValidatorPort {

    private final DeviceAgentServicePort deviceAgentServicePort;

    public DeviceValidatorPortImpl(final DeviceAgentServicePort deviceAgentServicePort) {
        this.deviceAgentServicePort = deviceAgentServicePort;
    }

    @Override
    public void validateDevice(final Device device) {
        final DeviceInformation deviceInformation = DeviceInformation.from(device);
        deviceAgentServicePort.validateDevice(deviceInformation);
    }
}
