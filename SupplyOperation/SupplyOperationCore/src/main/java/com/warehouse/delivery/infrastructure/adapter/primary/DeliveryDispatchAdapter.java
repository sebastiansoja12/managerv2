package com.warehouse.delivery.infrastructure.adapter.primary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.delivery.domain.model.Request;
import com.warehouse.delivery.domain.model.Response;
import com.warehouse.delivery.domain.port.primary.DeliveryPort;
import com.warehouse.delivery.domain.port.primary.TerminalRequestLoggerPort;
import com.warehouse.delivery.infrastructure.adapter.primary.creator.DeliveryCreator;
import com.warehouse.delivery.infrastructure.adapter.primary.dto.ErrorResponseDto;
import com.warehouse.delivery.infrastructure.adapter.primary.exception.RestException;
import com.warehouse.delivery.infrastructure.adapter.primary.mapper.DeliveryRequestMapper;
import com.warehouse.delivery.infrastructure.adapter.primary.mapper.DeliveryResponseMapper;
import com.warehouse.terminal.information.Device;
import com.warehouse.terminal.request.TerminalRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/deliveries")
public class DeliveryDispatchAdapter extends ProcessDispatcher {

    private final DeliveryPort deliveryPort;

    private final Set<DeliveryCreator> deliveryCreators;

    private final TerminalRequestLoggerPort terminalRequestLoggerPort;

    private final DeliveryRequestMapper requestMapper = getMapper(DeliveryRequestMapper.class);

    private final DeliveryResponseMapper responseMapper = getMapper(DeliveryResponseMapper.class);

    public DeliveryDispatchAdapter(final List<ProcessHandler> handlers, final DeliveryPort deliveryPort,
                                   final Set<DeliveryCreator> deliveryCreators,
                                   final TerminalRequestLoggerPort terminalRequestLoggerPort) {
        super(handlers);
        this.deliveryPort = deliveryPort;
        this.deliveryCreators = deliveryCreators;
        this.terminalRequestLoggerPort = terminalRequestLoggerPort;
    }

    @PostMapping(produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> processRequest(@RequestBody final TerminalRequest terminalRequest) {

        final Device device = terminalRequest.getDevice();

        log.info("Detected request from Terminal device: ID - {}, Version - {}, Responsible User - {}, Department - {}",
                device.getDeviceId(), device.getVersion(),
                device.getUsername(), device.getDepartmentCode());

        logTerminalRequest(terminalRequest);

        logDeviceId(terminalRequest);

        logVersion(terminalRequest);

        final Request request = this.requestMapper.map(terminalRequest);

        final Response response = this.process(request);

        final DeliveryCreator deliveryCreator = determineDeliveryCreator(request.getProcessType());

        final Set<DeliveryResponse> deliveryResponses = this.deliveryPort.processDelivery(
                deliveryCreator.create(request, response)
        );

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
    public ResponseEntity<?> handleException(final RestException ex) {
        final ErrorResponseDto error = new ErrorResponseDto(LocalDateTime.now(), ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatusCode.valueOf(error.getStatus()));
    }
}
