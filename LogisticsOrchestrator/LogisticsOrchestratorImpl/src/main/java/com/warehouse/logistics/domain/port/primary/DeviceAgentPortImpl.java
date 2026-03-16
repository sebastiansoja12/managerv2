package com.warehouse.logistics.domain.port.primary;

import com.warehouse.logistics.domain.port.secondary.DeviceAgentServicePort;
import com.warehouse.terminal.DeviceInformation;

public class DeviceAgentPortImpl implements DeviceAgentPort {

    private final DeviceAgentServicePort deviceAgentServicePort;

    public DeviceAgentPortImpl(final DeviceAgentServicePort deviceAgentServicePort) {
        this.deviceAgentServicePort = deviceAgentServicePort;
    }

    @Override
    public void updateDeviceIfNeed(final DeviceInformation deviceInformation) {
        deviceAgentServicePort.updateDevice(deviceInformation);
    }
}
