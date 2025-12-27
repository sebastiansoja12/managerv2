package com.warehouse.logistics.infrastructure.adapter.primary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.logistics.domain.model.LogisticsRequest;
import com.warehouse.logistics.domain.model.LogisticsResponse;
import com.warehouse.logistics.domain.model.Request;
import com.warehouse.logistics.domain.model.Response;
import com.warehouse.logistics.domain.port.primary.*;
import com.warehouse.logistics.infrastructure.adapter.primary.creator.DeliveryCreator;
import com.warehouse.logistics.infrastructure.adapter.primary.dto.ErrorResponseDto;
import com.warehouse.logistics.infrastructure.adapter.primary.exception.RestException;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.LogisticsRequestMapper;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.LogisticsResponseMapper;
import com.warehouse.terminal.information.Device;
import com.warehouse.terminal.request.TerminalRequest;
import com.warehouse.terminal.response.TerminalResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/deliveries")
public class LogisticsDispatchAdapter extends ProcessDispatcher {

    private final LogisticsPort logisticsPort;

    private final Set<DeliveryCreator> deliveryCreators;

    private final TerminalRequestLoggerPort terminalRequestLoggerPort;

    private final SupplierValidatorPort supplierValidatorPort;

    private final DepartmentValidatorPort departmentValidatorPort;

    private final DeviceValidatorPort deviceValidatorPort;

    private final DeviceAgentPort deviceAgentPort;

    private final LogisticsRequestMapper requestMapper = getMapper(LogisticsRequestMapper.class);

    private final LogisticsResponseMapper responseMapper = getMapper(LogisticsResponseMapper.class);

    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd H:mm:ss", Locale.of("PL"));

    public LogisticsDispatchAdapter(final List<ProcessHandler> handlers,
                                    final LogisticsPort logisticsPort,
                                    final Set<DeliveryCreator> deliveryCreators,
                                    final TerminalRequestLoggerPort terminalRequestLoggerPort,
                                    final SupplierValidatorPort supplierValidatorPort,
                                    final DepartmentValidatorPort departmentValidatorPort,
                                    final DeviceValidatorPort deviceValidatorPort,
                                    final DeviceAgentPort deviceAgentPort) {
        super(handlers);
        this.logisticsPort = logisticsPort;
        this.deliveryCreators = deliveryCreators;
        this.terminalRequestLoggerPort = terminalRequestLoggerPort;
        this.supplierValidatorPort = supplierValidatorPort;
        this.departmentValidatorPort = departmentValidatorPort;
        this.deviceValidatorPort = deviceValidatorPort;
        this.deviceAgentPort = deviceAgentPort;
    }

    @PostMapping(produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<TerminalResponse> processRequest(@RequestBody final TerminalRequest terminalRequest) {

        final Device device = terminalRequest.getDevice();

        log.info("Detected request from Terminal device: ID - {}, Version - {}, Responsible User - {}, Department - {}",
                device.getDeviceId(), device.getVersion(),
                device.getUsername(), device.getDepartmentCode());

        deviceValidatorPort.validateDevice(device);

        deviceAgentPort.updateDeviceIfNeed(device);

        logDeviceInformation(terminalRequest);

        logTerminalRequest(terminalRequest);

        logDeviceId(terminalRequest);

        logVersion(terminalRequest);

        final Request request = this.requestMapper.map(terminalRequest);

        final Response response = this.process(request);

        final DeliveryCreator deliveryCreator = determineDeliveryCreator(request.getProcessType());

        final Set<LogisticsRequest> logisticsRequests = deliveryCreator.create(request, response);

        final Set<LogisticsResponse> logisticsResponses = this.logisticsPort.processDelivery(logisticsRequests);

        response.updateLogisticsResponse(logisticsResponses);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseMapper.map(response));
    }

    private void logDeviceInformation(final TerminalRequest terminalRequest) {
        log.info("Logging device information in tracker");
        terminalRequestLoggerPort.logDeviceInformation(terminalRequest);
    }

    private DeliveryCreator determineDeliveryCreator(final ProcessType processType) {
        return deliveryCreators.stream()
                .filter(deliveryCreator -> deliveryCreator.canHandle(processType))
                .findFirst()
                .orElseThrow();
    }

    private void logDeviceId(final TerminalRequest terminalRequest) {
        log.info("Logging terminal id in tracker");
        terminalRequestLoggerPort.logDeviceId(terminalRequest);
    }

    private void logTerminalRequest(final TerminalRequest terminalRequest) {
        log.info("Logging request in tracker");
        terminalRequestLoggerPort.logRequest(terminalRequest);
    }

    private void logVersion(final TerminalRequest terminalRequest) {
        log.info("Logging device version in tracker");
        terminalRequestLoggerPort.logVersion(terminalRequest);
    }

    @ExceptionHandler(RestException.class)
    @ResponsePayload
    public ResponseEntity<ErrorResponseDto> handleException(final RestException ex) {
        final ErrorResponseDto errorResponse =
                new ErrorResponseDto(LocalDateTime.now().format(dtf), ex.getCode(), ex.getMessage());
        return ResponseEntity.status(ex.getCode())
                .header("Content-Type", "application/xml")
                .body(errorResponse);
    }
}
