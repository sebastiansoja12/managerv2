package com.warehouse.logistics.infrastructure.adapter.primary;

import com.warehouse.logistics.application.port.primary.DeviceAgentPort;
import com.warehouse.logistics.application.port.primary.DeviceValidatorPort;
import com.warehouse.logistics.application.port.primary.ProcessInitializerPort;
import com.warehouse.logistics.application.port.primary.ShipmentCollectPort;
import com.warehouse.logistics.application.port.primary.result.ShipmentCollectResult;
import com.warehouse.logistics.configuration.LogisticsSoapWebServiceConfiguration;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.CollectMapper;
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
import com.warehouse.terminal.jaxb.CollectRequest;
import com.warehouse.terminal.jaxb.CollectResponse;
import com.warehouse.xmlconverter.XmlToStringService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Endpoint
public class CollectDispatchAdapter {

    private final ProcessInitializerPort processInitializerPort;

    private final DeviceValidatorPort deviceValidatorPort;

    private final DeviceAgentPort deviceAgentPort;

    private final ShipmentCollectPort shipmentCollectPort;

    private final CollectMapper collectMapper;

    private final ProcessHubEventPublisher processHubEventPublisher;

    private final XmlToStringService xmlToStringService;

    public CollectDispatchAdapter(final ProcessInitializerPort processInitializerPort,
                                  final DeviceValidatorPort deviceValidatorPort,
                                  final DeviceAgentPort deviceAgentPort,
                                  final ShipmentCollectPort shipmentCollectPort,
                                  final CollectMapper collectMapper,
                                  final ProcessHubEventPublisher processHubEventPublisher,
                                  final XmlToStringService xmlToStringService) {
        this.processInitializerPort = processInitializerPort;
        this.deviceValidatorPort = deviceValidatorPort;
        this.deviceAgentPort = deviceAgentPort;
        this.shipmentCollectPort = shipmentCollectPort;
        this.collectMapper = collectMapper;
        this.processHubEventPublisher = processHubEventPublisher;
        this.xmlToStringService = xmlToStringService;
    }

    @DeviceAccessValidator
    @PayloadRoot(namespace = LogisticsSoapWebServiceConfiguration.TERMINAL_NAMESPACE, localPart = "CollectRequest")
    @ResponsePayload
    public CollectResponse processCollectRequest(@RequestPayload final CollectRequest collectRequest) {
        final DeviceInformation deviceInformation = JaxbDeviceInformationMapper.map(collectRequest.getDeviceInformation());

        log.info("Detected collect request from device: ID - {}, Version - {}, Responsible User - {}, Department - {}",
                deviceInformation.getDeviceId().value(), deviceInformation.getVersion(),
                deviceInformation.getUsername(), deviceInformation.getDepartmentCode().value());

        final ProcessId processId = processInitializerPort.initializeProcess(deviceInformation,
                xmlToStringService.convertToString(collectRequest));
        LogisticsProcessContext.setProcessId(processId);
        log.info("Collect process initialized in orchestrator flow: {}", processId.value());

        deviceValidatorPort.validateDevice(processId, ProcessType.COLLECT, deviceInformation);

        deviceAgentPort.updateDeviceIfNeed(deviceInformation);

        final ShipmentCollectResult result = shipmentCollectPort.collect(collectMapper.map(processId, collectRequest));

        final CollectResponse collectResponse = collectMapper.map(processId, result);
        publishResponseChangedEvent(processId, collectResponse);
        return collectResponse;
    }

    private void publishResponseChangedEvent(final ProcessId processId, final CollectResponse collectResponse) {
        processHubEventPublisher.publish(new ProcessResponseChangedEvent(processId, ServiceType.LOGISTICS_ORCHESTRATOR,
                LocalDateTime.now(), xmlToStringService.convertToString(collectResponse)));
    }
}
