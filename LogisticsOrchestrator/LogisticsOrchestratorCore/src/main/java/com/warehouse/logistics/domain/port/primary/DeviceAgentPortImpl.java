package com.warehouse.logistics.domain.port.primary;

import com.warehouse.logistics.domain.port.secondary.DeviceAgentServicePort;
import com.warehouse.terminal.DeviceInformation;
import com.warehouse.terminal.information.Device;

public class DeviceAgentPortImpl implements DeviceAgentPort {

    private final DeviceAgentServicePort deviceAgentServicePort;

    public DeviceAgentPortImpl(final DeviceAgentServicePort deviceAgentServicePort) {
        this.deviceAgentServicePort = deviceAgentServicePort;
    }

    @Override
    public void updateDeviceIfNeed(final Device device) {
        final DeviceInformation deviceInformation = DeviceInformation.from(device);
        deviceAgentServicePort.updateDevice(deviceInformation);
    }
}
