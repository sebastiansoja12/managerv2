package com.warehouse.logistics.domain.port.primary;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.logistics.domain.model.DeviceValidateCommand;
import com.warehouse.logistics.domain.port.secondary.DeviceAgentServicePort;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.JaxbDeviceInformationMapper;
import com.warehouse.terminal.jaxb.TerminalRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeviceValidatorPortImpl implements DeviceValidatorPort {

    private final DeviceAgentServicePort deviceAgentServicePort;

    public DeviceValidatorPortImpl(final DeviceAgentServicePort deviceAgentServicePort) {
        this.deviceAgentServicePort = deviceAgentServicePort;
    }

    @Override
    public void validateDevice(final ProcessId processId, final TerminalRequest request) {
        final DeviceValidateCommand command = new DeviceValidateCommand(
                processId, ProcessType.valueOf(request.getProcessType().name()), JaxbDeviceInformationMapper.map(request.getDevice()));
        deviceAgentServicePort.validateDevice(command);
    }
}
