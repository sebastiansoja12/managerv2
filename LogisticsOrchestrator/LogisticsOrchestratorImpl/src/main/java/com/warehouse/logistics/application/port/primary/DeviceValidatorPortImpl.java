package com.warehouse.logistics.application.port.primary;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.logistics.application.port.secondary.DeviceAgentServicePort;
import com.warehouse.logistics.application.port.secondary.DeviceValidateCommand;
import com.warehouse.terminal.DeviceInformation;

public class DeviceValidatorPortImpl implements DeviceValidatorPort {

    private final DeviceAgentServicePort deviceAgentServicePort;

    public DeviceValidatorPortImpl(final DeviceAgentServicePort deviceAgentServicePort) {
        this.deviceAgentServicePort = deviceAgentServicePort;
    }

    @Override
    public void validateDevice(final ProcessId processId,
                               final ProcessType processType,
                               final DeviceInformation deviceInformation) {
        deviceAgentServicePort.validateDevice(new DeviceValidateCommand(processId, processType, deviceInformation));
    }
}
