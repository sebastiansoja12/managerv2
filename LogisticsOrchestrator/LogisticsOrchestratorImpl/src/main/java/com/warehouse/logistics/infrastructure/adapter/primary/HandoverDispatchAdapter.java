package com.warehouse.logistics.infrastructure.adapter.primary;

import com.warehouse.logistics.application.port.primary.DeviceAgentPort;
import com.warehouse.logistics.application.port.primary.DeviceValidatorPort;
import com.warehouse.logistics.application.port.primary.ProcessInitializerPort;
import com.warehouse.logistics.application.port.primary.ShipmentHandoverPort;
import com.warehouse.logistics.application.port.primary.result.ShipmentHandoverResult;
import com.warehouse.logistics.configuration.LogisticsSoapWebServiceConfiguration;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.HandoverMapper;
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
import com.warehouse.terminal.jaxb.HandoverRequest;
import com.warehouse.terminal.jaxb.HandoverResponse;
import com.warehouse.xmlconverter.XmlToStringService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Endpoint
public class HandoverDispatchAdapter {

    private final ProcessInitializerPort processInitializerPort;

    private final DeviceValidatorPort deviceValidatorPort;

    private final DeviceAgentPort deviceAgentPort;

    private final ShipmentHandoverPort shipmentHandoverPort;

    private final HandoverMapper handoverMapper;

    private final ProcessHubEventPublisher processHubEventPublisher;

    private final XmlToStringService xmlToStringService;

    public HandoverDispatchAdapter(final ProcessInitializerPort processInitializerPort,
                                   final DeviceValidatorPort deviceValidatorPort,
                                   final DeviceAgentPort deviceAgentPort,
                                   final ShipmentHandoverPort shipmentHandoverPort,
                                   final HandoverMapper handoverMapper,
                                   final ProcessHubEventPublisher processHubEventPublisher,
                                   final XmlToStringService xmlToStringService) {
        this.processInitializerPort = processInitializerPort;
        this.deviceValidatorPort = deviceValidatorPort;
        this.deviceAgentPort = deviceAgentPort;
        this.shipmentHandoverPort = shipmentHandoverPort;
        this.handoverMapper = handoverMapper;
        this.processHubEventPublisher = processHubEventPublisher;
        this.xmlToStringService = xmlToStringService;
    }

    @DeviceAccessValidator
    @PayloadRoot(namespace = LogisticsSoapWebServiceConfiguration.TERMINAL_NAMESPACE, localPart = "HandoverRequest")
    @ResponsePayload
    public HandoverResponse processHandoverRequest(@RequestPayload final HandoverRequest handoverRequest) {
        final DeviceInformation deviceInformation = JaxbDeviceInformationMapper.map(handoverRequest.getDeviceInformation());

        log.info("Detected handover request from device: ID - {}, Version - {}, Responsible User - {}, Department - {}",
                deviceInformation.getDeviceId().value(), deviceInformation.getVersion(),
                deviceInformation.getUsername(), deviceInformation.getDepartmentCode().value());

        final ProcessId processId = processInitializerPort.initializeProcess(deviceInformation,
                xmlToStringService.convertToString(handoverRequest));
        LogisticsProcessContext.setProcessId(processId);
        log.info("Handover process initialized in orchestrator flow: {}", processId.value());

        deviceValidatorPort.validateDevice(processId, ProcessType.HANDOVER, deviceInformation);

        deviceAgentPort.updateDeviceIfNeed(deviceInformation);

        final ShipmentHandoverResult result = shipmentHandoverPort.handOver(handoverMapper.map(processId, handoverRequest));

        final HandoverResponse handoverResponse = handoverMapper.map(processId, result);
        publishResponseChangedEvent(processId, handoverResponse);
        return handoverResponse;
    }

    private void publishResponseChangedEvent(final ProcessId processId, final HandoverResponse handoverResponse) {
        processHubEventPublisher.publish(new ProcessResponseChangedEvent(processId, ServiceType.LOGISTICS_ORCHESTRATOR,
                LocalDateTime.now(), xmlToStringService.convertToString(handoverResponse)));
    }
}
