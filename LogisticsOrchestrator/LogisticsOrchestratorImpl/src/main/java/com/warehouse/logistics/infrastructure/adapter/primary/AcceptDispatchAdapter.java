package com.warehouse.logistics.infrastructure.adapter.primary;

import com.warehouse.logistics.application.port.primary.DeviceAgentPort;
import com.warehouse.logistics.application.port.primary.DeviceValidatorPort;
import com.warehouse.logistics.application.port.primary.ProcessInitializerPort;
import com.warehouse.logistics.application.port.primary.ShipmentAcceptPort;
import com.warehouse.logistics.application.port.primary.result.ShipmentAcceptResult;
import com.warehouse.logistics.configuration.LogisticsSoapWebServiceConfiguration;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.AcceptMapper;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.JaxbDeviceInformationMapper;
import java.time.LocalDateTime;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.validator.DeviceAccessValidator;
import com.warehouse.process.ProcessHubEventPublisher;
import com.warehouse.process.infrastructure.event.ProcessResponseChangedEvent;
import com.warehouse.terminal.DeviceInformation;
import com.warehouse.terminal.jaxb.AcceptRequest;
import com.warehouse.terminal.jaxb.AcceptResponse;
import com.warehouse.xmlconverter.XmlToStringService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Endpoint
public class AcceptDispatchAdapter {

    private final ProcessInitializerPort processInitializerPort;

    private final DeviceValidatorPort deviceValidatorPort;

    private final DeviceAgentPort deviceAgentPort;

    private final ShipmentAcceptPort shipmentAcceptPort;

    private final AcceptMapper acceptMapper;

    private final ProcessHubEventPublisher processHubEventPublisher;

    private final XmlToStringService xmlToStringService;

    public AcceptDispatchAdapter(final ProcessInitializerPort processInitializerPort,
                                  final DeviceValidatorPort deviceValidatorPort,
                                  final DeviceAgentPort deviceAgentPort,
                                  final ShipmentAcceptPort shipmentAcceptPort,
                                  final AcceptMapper acceptMapper,
                                  final ProcessHubEventPublisher processHubEventPublisher,
                                  final XmlToStringService xmlToStringService) {
        this.processInitializerPort = processInitializerPort;
        this.deviceValidatorPort = deviceValidatorPort;
        this.deviceAgentPort = deviceAgentPort;
        this.shipmentAcceptPort = shipmentAcceptPort;
        this.acceptMapper = acceptMapper;
        this.processHubEventPublisher = processHubEventPublisher;
        this.xmlToStringService = xmlToStringService;
    }

    @DeviceAccessValidator
    @PayloadRoot(namespace = LogisticsSoapWebServiceConfiguration.TERMINAL_NAMESPACE, localPart = "AcceptRequest")
    @ResponsePayload
    public AcceptResponse processAcceptRequest(@RequestPayload final AcceptRequest acceptRequest) {
        final DeviceInformation deviceInformation = JaxbDeviceInformationMapper.map(acceptRequest.getDeviceInformation());

        log.info("Detected accept request from device: ID - {}, Version - {}, Responsible User - {}, Department - {}",
                deviceInformation.getDeviceId().value(), deviceInformation.getVersion(),
                deviceInformation.getUsername(), deviceInformation.getDepartmentCode().value());

        final ProcessId processId = processInitializerPort.initializeProcess(deviceInformation,
                xmlToStringService.convertToString(acceptRequest));
        LogisticsProcessContext.setProcessId(processId);
        log.info("Accept process initialized in orchestrator flow: {}", processId.value());

        deviceValidatorPort.validateDevice(processId, ProcessType.ACCEPT, deviceInformation);

        deviceAgentPort.updateDeviceIfNeed(deviceInformation);

        final ShipmentAcceptResult result = shipmentAcceptPort.accept(acceptMapper.map(processId, acceptRequest));

        final AcceptResponse acceptResponse = acceptMapper.map(processId, result);
        publishResponseChangedEvent(processId, acceptResponse);
        return acceptResponse;
    }

    private void publishResponseChangedEvent(final ProcessId processId, final AcceptResponse acceptResponse) {
        processHubEventPublisher.publish(new ProcessResponseChangedEvent(processId, ServiceType.LOGISTICS_ORCHESTRATOR,
                LocalDateTime.now(), xmlToStringService.convertToString(acceptResponse)));
    }
}
