package com.warehouse.delivery.infrastructure.adapter.primary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.delivery.domain.model.Request;
import com.warehouse.delivery.domain.model.Response;
import com.warehouse.delivery.domain.port.primary.DeliveryPort;
import com.warehouse.delivery.domain.port.primary.SupplierValidatorPort;
import com.warehouse.delivery.domain.port.primary.TerminalRequestLoggerPort;
import com.warehouse.delivery.infrastructure.adapter.primary.creator.DeliveryCreator;
import com.warehouse.delivery.infrastructure.adapter.primary.dto.ErrorResponseDto;
import com.warehouse.delivery.infrastructure.adapter.primary.exception.RestException;
import com.warehouse.delivery.infrastructure.adapter.primary.mapper.DeliveryRequestMapper;
import com.warehouse.delivery.infrastructure.adapter.primary.mapper.DeliveryResponseMapper;
import com.warehouse.terminal.information.Device;
import com.warehouse.terminal.request.TerminalRequest;
import com.warehouse.terminal.response.TerminalResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/deliveries")
public class DeliveryDispatchAdapter extends ProcessDispatcher {


    private final DeliveryPort deliveryPort;

    private final Set<DeliveryCreator> deliveryCreators;

    private final TerminalRequestLoggerPort terminalRequestLoggerPort;

    private final SupplierValidatorPort supplierValidatorPort;

    private final DeliveryRequestMapper requestMapper = getMapper(DeliveryRequestMapper.class);

    private final DeliveryResponseMapper responseMapper = getMapper(DeliveryResponseMapper.class);

    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd H:mm:ss", Locale.of("PL"));

    public DeliveryDispatchAdapter(final List<ProcessHandler> handlers,
                                   final DeliveryPort deliveryPort,
                                   final Set<DeliveryCreator> deliveryCreators,
                                   final TerminalRequestLoggerPort terminalRequestLoggerPort,
                                   final SupplierValidatorPort supplierValidatorPort) {
        super(handlers);
        this.deliveryPort = deliveryPort;
        this.deliveryCreators = deliveryCreators;
        this.terminalRequestLoggerPort = terminalRequestLoggerPort;
        this.supplierValidatorPort = supplierValidatorPort;
    }

    @PostMapping(produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<TerminalResponse> processRequest(@RequestBody final TerminalRequest terminalRequest) {

        final Device device = terminalRequest.getDevice();

        supplierValidatorPort.validateSupplierCode(device.getUsername());

        log.info("Detected request from Terminal device: ID - {}, Version - {}, Responsible User - {}, Department - {}",
                device.getDeviceId(), device.getVersion(),
                device.getUsername(), device.getDepartmentCode());

        logTerminalRequest(terminalRequest);

        logDeviceId(terminalRequest);

        logVersion(terminalRequest);

        final Request request = this.requestMapper.map(terminalRequest);

        final Response response = this.process(request);

        final DeliveryCreator deliveryCreator = determineDeliveryCreator(request.getProcessType());

        final Set<DeliveryRequest> deliveryRequests = deliveryCreator.create(request, response);

        final Set<DeliveryResponse> deliveryResponses =
                !CollectionUtils.isEmpty(deliveryRequests) ? this.deliveryPort.processDelivery(deliveryRequests)
                : Collections.emptySet();

        response.updateDeliveryResponse(deliveryResponses);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseMapper.map(response));
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
