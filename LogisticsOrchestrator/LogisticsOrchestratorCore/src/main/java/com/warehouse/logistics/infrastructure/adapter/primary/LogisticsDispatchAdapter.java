package com.warehouse.logistics.infrastructure.adapter.primary;

import java.util.List;
import java.util.Set;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.logistics.configuration.LogisticsSoapWebServiceConfiguration;
import com.warehouse.logistics.domain.model.DeviceValidateCommand;
import com.warehouse.logistics.domain.model.LogisticsRequest;
import com.warehouse.logistics.domain.model.LogisticsResponse;
import com.warehouse.logistics.domain.model.Request;
import com.warehouse.logistics.domain.model.Response;
import com.warehouse.logistics.domain.port.primary.*;
import com.warehouse.logistics.infrastructure.adapter.primary.creator.DeliveryCreator;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.JaxbDeviceInformationMapper;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.LogisticsRequestMapper;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.LogisticsResponseMapper;
import com.warehouse.terminal.jaxb.DeviceType;
import com.warehouse.terminal.jaxb.TerminalRequest;
import com.warehouse.terminal.response.TerminalResponse;

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

    public LogisticsDispatchAdapter(final List<ProcessHandler> handlers,
                                    final LogisticsPort logisticsPort,
                                    final Set<DeliveryCreator> deliveryCreators,
                                    final TerminalRequestLoggerPort terminalRequestLoggerPort,
                                    final SupplierValidatorPort supplierValidatorPort,
                                    final DepartmentValidatorPort departmentValidatorPort,
                                    final DeviceValidatorPort deviceValidatorPort,
                                    final DeviceAgentPort deviceAgentPort,
                                    final ProcessInitializerPort processInitializerPort) {
        super(handlers);
        this.logisticsPort = logisticsPort;
        this.deliveryCreators = deliveryCreators;
        this.terminalRequestLoggerPort = terminalRequestLoggerPort;
        this.supplierValidatorPort = supplierValidatorPort;
        this.departmentValidatorPort = departmentValidatorPort;
        this.deviceValidatorPort = deviceValidatorPort;
        this.deviceAgentPort = deviceAgentPort;
        this.processInitializerPort = processInitializerPort;
    }

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

        return responseMapper.map(response);
    }

    private DeliveryCreator determineDeliveryCreator(final ProcessType processType) {
        return deliveryCreators.stream()
                .filter(deliveryCreator -> deliveryCreator.canHandle(processType))
                .findFirst()
                .orElseThrow();
    }
}
