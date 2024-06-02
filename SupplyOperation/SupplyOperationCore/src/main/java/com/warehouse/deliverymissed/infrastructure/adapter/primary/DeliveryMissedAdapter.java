package com.warehouse.deliverymissed.infrastructure.adapter.primary;


import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.deliverymissed.domain.port.primary.TerminalRequestLoggerPort;
import com.warehouse.deliverymissed.infrastructure.adapter.primary.dto.ErrorResponseDto;
import com.warehouse.deliverymissed.infrastructure.adapter.primary.dto.exception.RestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.deliverymissed.domain.model.DeliveryMissedRequest;
import com.warehouse.deliverymissed.domain.port.primary.DeliveryMissedPort;
import com.warehouse.deliverymissed.domain.port.primary.SupplierValidatorPort;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedResponse;
import com.warehouse.deliverymissed.infrastructure.adapter.primary.mapper.DeliveryMissedRequestMapper;
import com.warehouse.deliverymissed.infrastructure.adapter.primary.mapper.DeliveryMissedResponseMapper;
import com.warehouse.terminal.information.TerminalDeviceInformation;
import com.warehouse.terminal.request.TerminalRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/delivery-missings")
@RequiredArgsConstructor
@Slf4j
public class DeliveryMissedAdapter {

    private final DeliveryMissedRequestMapper requestMapper = getMapper(DeliveryMissedRequestMapper.class);

    private final DeliveryMissedResponseMapper responseMapper = getMapper(DeliveryMissedResponseMapper.class);

    private final DeliveryMissedPort deliveryMissedPort;

    private final TerminalRequestLoggerPort terminalRequestLoggerPort;

    private final SupplierValidatorPort validatorPort;

    @PostMapping(produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> deliveryMiss(@RequestBody final TerminalRequest terminalRequest) {

        final TerminalDeviceInformation terminalDeviceInformation = terminalRequest.getTerminalDeviceInformation();

        log.info("Detected request from Terminal device: ID - {}, Version - {}, Responsible User - {}, Depot - {}",
                terminalDeviceInformation.getTerminalId(), terminalDeviceInformation.getVersion(),
                terminalDeviceInformation.getUsername(), terminalDeviceInformation.getDepotCode());

        validatorPort.validateSupplierCode(terminalDeviceInformation.getUsername());

        logTerminalRequest(terminalRequest);

        logTerminalId(terminalRequest);

        logVersion(terminalRequest);

        final DeliveryMissedRequest request = requestMapper.map(terminalRequest);

        final DeliveryMissedResponse response = deliveryMissedPort.logMissedDelivery(request);

        return new ResponseEntity<>(responseMapper.map(response), HttpStatus.CREATED);
    }

    private void logTerminalId(TerminalRequest terminalRequest) {
        log.info("Logging terminal id in tracker");
        terminalRequestLoggerPort.logTerminalId(terminalRequest);
    }

    private void logTerminalRequest(final TerminalRequest terminalRequest) {
        log.info("Logging request in tracker");
        terminalRequestLoggerPort.logDeliveryMissedTerminalRequest(terminalRequest);
    }

    private void logVersion(final TerminalRequest terminalRequest) {
        log.info("Logging device version in tracker");
        terminalRequestLoggerPort.logVersion(terminalRequest);
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<?> handleException(final RestException ex) {
        final ErrorResponseDto error = ErrorResponseDto.builder()
                .error(ex.getMessage())
                .status(ex.getCode())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatusCode.valueOf(error.getStatus()));
    }
}
