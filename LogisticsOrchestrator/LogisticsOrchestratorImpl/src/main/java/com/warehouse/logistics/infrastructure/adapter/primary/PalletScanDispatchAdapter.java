package com.warehouse.logistics.infrastructure.adapter.primary;

import java.time.LocalDateTime;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.validator.DeviceAccessValidator;
import com.warehouse.logistics.configuration.LogisticsSoapWebServiceConfiguration;
import com.warehouse.logistics.domain.port.primary.DeviceAgentPort;
import com.warehouse.logistics.domain.port.primary.DeviceValidatorPort;
import com.warehouse.logistics.domain.port.primary.PalletScanPort;
import com.warehouse.logistics.domain.port.primary.ProcessInitializerPort;
import com.warehouse.logistics.domain.vo.PalletScanOutcome;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.JaxbDeviceInformationMapper;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.PalletScanMapper;
import com.warehouse.process.ProcessHubEventPublisher;
import com.warehouse.process.infrastructure.event.ProcessResponseChangedEvent;
import com.warehouse.terminal.DeviceInformation;
import com.warehouse.terminal.jaxb.PalletScanRequest;
import com.warehouse.terminal.jaxb.PalletScanResponse;
import com.warehouse.xmlconverter.XmlToStringService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Endpoint
public class PalletScanDispatchAdapter {

    private final ProcessInitializerPort processInitializerPort;

    private final DeviceValidatorPort deviceValidatorPort;

    private final DeviceAgentPort deviceAgentPort;

    private final PalletScanPort palletScanPort;

    private final PalletScanMapper palletScanMapper;

    private final ProcessHubEventPublisher processHubEventPublisher;

    private final XmlToStringService xmlToStringService;

    public PalletScanDispatchAdapter(final ProcessInitializerPort processInitializerPort,
                                     final DeviceValidatorPort deviceValidatorPort,
                                     final DeviceAgentPort deviceAgentPort,
                                     final PalletScanPort palletScanPort,
                                     final PalletScanMapper palletScanMapper,
                                     final ProcessHubEventPublisher processHubEventPublisher,
                                     final XmlToStringService xmlToStringService) {
        this.processInitializerPort = processInitializerPort;
        this.deviceValidatorPort = deviceValidatorPort;
        this.deviceAgentPort = deviceAgentPort;
        this.palletScanPort = palletScanPort;
        this.palletScanMapper = palletScanMapper;
        this.processHubEventPublisher = processHubEventPublisher;
        this.xmlToStringService = xmlToStringService;
    }

    @DeviceAccessValidator
    @PayloadRoot(namespace = LogisticsSoapWebServiceConfiguration.TERMINAL_NAMESPACE, localPart = "PalletScanRequest")
    @ResponsePayload
    public PalletScanResponse processPalletScanRequest(@RequestPayload final PalletScanRequest palletScanRequest) {
        final DeviceInformation deviceInformation =
                JaxbDeviceInformationMapper.map(palletScanRequest.getDeviceInformation());

        log.info("Detected pallet scan request from device: ID - {}, Version - {}, Responsible User - {}, Department - {}",
                deviceInformation.getDeviceId().value(), deviceInformation.getVersion(),
                deviceInformation.getUsername(), deviceInformation.getDepartmentCode().value());

        final ProcessId processId = processInitializerPort.initializeProcess(deviceInformation,
                xmlToStringService.convertToString(palletScanRequest));
        LogisticsProcessContext.setProcessId(processId);
        log.info("Pallet scan process initialized in orchestrator flow: {}", processId.value());

        deviceValidatorPort.validateDevice(processId, ProcessType.PALLET_SCAN, deviceInformation);

        deviceAgentPort.updateDeviceIfNeed(deviceInformation);

        final PalletScanOutcome outcome = palletScanPort.scan(palletScanMapper.map(processId, palletScanRequest));

        final PalletScanResponse palletScanResponse = palletScanMapper.map(processId, outcome);
        publishResponseChangedEvent(processId, palletScanResponse);
        return palletScanResponse;
    }

    private void publishResponseChangedEvent(final ProcessId processId, final PalletScanResponse palletScanResponse) {
        processHubEventPublisher.publish(new ProcessResponseChangedEvent(processId, ServiceType.LOGISTICS_ORCHESTRATOR,
                LocalDateTime.now(), xmlToStringService.convertToString(palletScanResponse)));
    }
}
