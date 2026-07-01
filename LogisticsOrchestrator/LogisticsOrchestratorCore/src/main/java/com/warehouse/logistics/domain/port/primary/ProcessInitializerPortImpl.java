package com.warehouse.logistics.domain.port.primary;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.logistics.domain.port.secondary.ProcessHubServicePort;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.JaxbDeviceInformationMapper;
import com.warehouse.terminal.DeviceInformation;
import com.warehouse.terminal.jaxb.TerminalRequest;
import com.warehouse.xmlconverter.XmlToStringService;

public class ProcessInitializerPortImpl implements ProcessInitializerPort {

    private final ProcessHubServicePort processHubServicePort;

    private final XmlToStringService xmlToStringService;

    public ProcessInitializerPortImpl(final ProcessHubServicePort processHubServicePort,
                                      final XmlToStringService xmlToStringService) {
        this.processHubServicePort = processHubServicePort;
        this.xmlToStringService = xmlToStringService;
    }

    @Override
    public ProcessId initializeProcess(final TerminalRequest request) {
        final DeviceInformation deviceInformation = JaxbDeviceInformationMapper.map(request.getDevice());
        return this.processHubServicePort.initialize(deviceInformation, xmlToStringService.convertToString(request));
    }
}
