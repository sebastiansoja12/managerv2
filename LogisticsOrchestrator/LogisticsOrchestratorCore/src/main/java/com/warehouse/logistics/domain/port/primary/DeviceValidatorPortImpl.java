package com.warehouse.logistics.domain.port.primary;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.logistics.domain.port.secondary.DeviceAgentServicePort;
import com.warehouse.terminal.DeviceInformation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeviceValidatorPortImpl implements DeviceValidatorPort {

    private final DeviceAgentServicePort deviceAgentServicePort;

    public DeviceValidatorPortImpl(final DeviceAgentServicePort deviceAgentServicePort) {
        this.deviceAgentServicePort = deviceAgentServicePort;
    }

    @Override
    public void validateDevice(final ProcessId processId,
                               final DeviceInformation deviceInformation) {
        deviceAgentServicePort.validateDevice(processId, deviceInformation);
    }
}
