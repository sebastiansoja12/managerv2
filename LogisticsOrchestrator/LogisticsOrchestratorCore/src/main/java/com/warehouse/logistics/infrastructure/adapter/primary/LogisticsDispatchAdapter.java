package com.warehouse.logistics.infrastructure.adapter.primary;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.validator.DeviceAccessValidator;
import com.warehouse.logistics.configuration.LogisticsSoapWebServiceConfiguration;
import com.warehouse.logistics.domain.model.LogisticsRequest;
import com.warehouse.logistics.domain.model.LogisticsResponse;
import com.warehouse.logistics.domain.model.Request;
import com.warehouse.logistics.domain.model.Response;
import com.warehouse.logistics.domain.port.primary.DeviceAgentPort;
import com.warehouse.logistics.domain.port.primary.DeviceValidatorPort;
import com.warehouse.logistics.domain.port.primary.LogisticsPort;
import com.warehouse.logistics.domain.port.primary.ProcessInitializerPort;
import com.warehouse.logistics.infrastructure.adapter.primary.creator.DeliveryCreator;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.JaxbDeviceInformationMapper;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.LogisticsRequestMapper;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.LogisticsResponseMapper;
import com.warehouse.process.ProcessHubEventPublisher;
import com.warehouse.process.infrastructure.event.ProcessResponseChangedEvent;
import com.warehouse.terminal.jaxb.DeviceType;
import com.warehouse.terminal.jaxb.TerminalRequest;
import com.warehouse.terminal.response.TerminalResponse;
import com.warehouse.xmlconverter.XmlToStringService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Endpoint
public class LogisticsDispatchAdapter extends ProcessDispatcher {

    private final LogisticsPort logisticsPort;

    private final Set<DeliveryCreator> deliveryCreators;

    private final DeviceValidatorPort deviceValidatorPort;

    private final DeviceAgentPort deviceAgentPort;

    private final LogisticsRequestMapper requestMapper;

    private final LogisticsResponseMapper responseMapper;

    private final ProcessInitializerPort processInitializerPort;

    private final ProcessHubEventPublisher processHubEventPublisher;

    private final XmlToStringService xmlToStringService;

    public LogisticsDispatchAdapter(final List<ProcessHandler> handlers,
                                    final LogisticsPort logisticsPort,
                                    final Set<DeliveryCreator> deliveryCreators,
                                    final DeviceValidatorPort deviceValidatorPort,
                                    final DeviceAgentPort deviceAgentPort,
                                    final LogisticsRequestMapper requestMapper,
                                    final LogisticsResponseMapper responseMapper,
                                    final ProcessInitializerPort processInitializerPort,
                                    final ProcessHubEventPublisher processHubEventPublisher,
                                    final XmlToStringService xmlToStringService) {
        super(handlers);
        this.logisticsPort = logisticsPort;
        this.deliveryCreators = deliveryCreators;
        this.deviceValidatorPort = deviceValidatorPort;
        this.deviceAgentPort = deviceAgentPort;
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
        this.processInitializerPort = processInitializerPort;
        this.processHubEventPublisher = processHubEventPublisher;
        this.xmlToStringService = xmlToStringService;
    }

    @DeviceAccessValidator
    @PayloadRoot(namespace = LogisticsSoapWebServiceConfiguration.TERMINAL_NAMESPACE, localPart = "TerminalRequest")
    @ResponsePayload
    public TerminalResponse processRequestWsdl(@RequestPayload final TerminalRequest terminalRequest) {
        final DeviceType device = terminalRequest.getDevice();

        log.info("Detected request from Terminal device: ID - {}, Version - {}, Responsible User - {}, Department - {}",
                device.getDeviceID(), device.getVersion(),
                device.getResponsibleUser(), device.getDepartmentCode());

        final ProcessId processId = this.processInitializerPort.initializeProcess(terminalRequest);
        LogisticsProcessContext.setProcessId(processId);
        log.info("Process initialized in orchestrator flow: {}", processId.value());

        deviceValidatorPort.validateDevice(processId, terminalRequest);

        deviceAgentPort.updateDeviceIfNeed(JaxbDeviceInformationMapper.map(device));

        final Request request = this.requestMapper.map(terminalRequest);

        final Response response = this.process(processId, request);

        final DeliveryCreator deliveryCreator = determineDeliveryCreator(request.getProcessType());

        final Set<LogisticsRequest> logisticsRequests = deliveryCreator.create(request, response);

        final Set<LogisticsResponse> logisticsResponses = this.logisticsPort.processDelivery(logisticsRequests);

        response.updateLogisticsResponse(logisticsResponses);

        return buildTerminalResponse(processId, response);
    }

    private TerminalResponse buildTerminalResponse(final ProcessId processId, final Response response) {
        final TerminalResponse terminalResponse = responseMapper.map(processId, response);
        publishResponseChangedEvent(processId, terminalResponse);
        return terminalResponse;
    }

    private void publishResponseChangedEvent(final ProcessId processId, final TerminalResponse terminalResponse) {
        final ProcessResponseChangedEvent event = new ProcessResponseChangedEvent(processId, ServiceType.LOGISTICS_ORCHESTRATOR,
                LocalDateTime.now(), xmlToStringService.convertToString(terminalResponse));
        processHubEventPublisher.publish(event);
    }

    private DeliveryCreator determineDeliveryCreator(final ProcessType processType) {
        return deliveryCreators.stream()
                .filter(deliveryCreator -> deliveryCreator.canHandle(processType))
                .findFirst()
                .orElseThrow();
    }
}
