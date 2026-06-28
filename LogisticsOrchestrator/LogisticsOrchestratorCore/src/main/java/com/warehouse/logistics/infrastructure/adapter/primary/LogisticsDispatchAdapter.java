package com.warehouse.logistics.infrastructure.adapter.primary;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.warehouse.commonassets.validator.DeviceAccessValidator;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.logistics.configuration.LogisticsSoapWebServiceConfiguration;
import com.warehouse.logistics.domain.model.*;
import com.warehouse.logistics.domain.port.primary.*;
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

    private final TerminalRequestLoggerPort terminalRequestLoggerPort;

    private final SupplierValidatorPort supplierValidatorPort;

    private final DepartmentValidatorPort departmentValidatorPort;

    private final DeviceValidatorPort deviceValidatorPort;

    private final DeviceAgentPort deviceAgentPort;

    private final LogisticsRequestMapper requestMapper = new LogisticsRequestMapper();

    private final LogisticsResponseMapper responseMapper = new LogisticsResponseMapper();

    private final ProcessInitializerPort processInitializerPort;

    private final ProcessHubEventPublisher processHubEventPublisher;

    private final XmlToStringService xmlToStringService;

    public LogisticsDispatchAdapter(final List<ProcessHandler> handlers,
                                    final LogisticsPort logisticsPort,
                                    final Set<DeliveryCreator> deliveryCreators,
                                    final TerminalRequestLoggerPort terminalRequestLoggerPort,
                                    final SupplierValidatorPort supplierValidatorPort,
                                    final DepartmentValidatorPort departmentValidatorPort,
                                    final DeviceValidatorPort deviceValidatorPort,
                                    final DeviceAgentPort deviceAgentPort,
                                    final ProcessInitializerPort processInitializerPort,
                                    final ProcessHubEventPublisher processHubEventPublisher,
                                    final XmlToStringService xmlToStringService) {
        super(handlers);
        this.logisticsPort = logisticsPort;
        this.deliveryCreators = deliveryCreators;
        this.terminalRequestLoggerPort = terminalRequestLoggerPort;
        this.supplierValidatorPort = supplierValidatorPort;
        this.departmentValidatorPort = departmentValidatorPort;
        this.deviceValidatorPort = deviceValidatorPort;
        this.deviceAgentPort = deviceAgentPort;
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
        log.info("Process initialized in orchestrator flow: {}", processId.value());

        deviceValidatorPort.validateDevice(new DeviceValidateCommand(
                processId,
                ProcessType.valueOf(terminalRequest.getProcessType().name()),
                JaxbDeviceInformationMapper.map(device)));

        deviceAgentPort.updateDeviceIfNeed(JaxbDeviceInformationMapper.map(device));

        final Request request = this.requestMapper.map(terminalRequest);

        final Response response = this.process(request);

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
