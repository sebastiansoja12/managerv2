package com.warehouse.deliveryreturn.infrastructure.adapter.primary;


import static com.warehouse.commonassets.enumeration.ProcessType.RETURN;
import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.model.Request;
import com.warehouse.delivery.domain.model.Response;
import com.warehouse.delivery.infrastructure.adapter.primary.ProcessHandler;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnRequest;
import com.warehouse.deliveryreturn.domain.port.primary.DeliveryReturnPort;
import com.warehouse.deliveryreturn.domain.port.primary.SupplierCodeValidatorPort;
import com.warehouse.deliveryreturn.domain.port.primary.TerminalRequestLoggerPort;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponse;
import com.warehouse.deliveryreturn.infrastructure.adapter.primary.mapper.DeliveryReturnRequestMapper;
import com.warehouse.deliveryreturn.infrastructure.adapter.primary.mapper.DeliveryReturnResponseMapper;
import com.warehouse.terminal.information.Device;
import com.warehouse.terminal.request.TerminalRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/delivery-returns")
@RequiredArgsConstructor
@Slf4j
public class DeliveryReturnAdapter implements ProcessHandler {

    private final DeliveryReturnPort deliveryReturnPort;

    private final SupplierCodeValidatorPort validatorPort;

    private final TerminalRequestLoggerPort terminalRequestLoggerPort;

    private final DeliveryReturnRequestMapper requestMapper = getMapper(DeliveryReturnRequestMapper.class);

    private final DeliveryReturnResponseMapper responseMapper = getMapper(DeliveryReturnResponseMapper.class);

    @PostMapping(produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> deliveryReturn(@RequestBody final TerminalRequest terminalRequest) {

        final Device deviceInformation = terminalRequest.getDevice();

        log.info("Detected request from Terminal device: ID - {}, Version - {}, Responsible User - {}, Depot - {}",
                deviceInformation.getDeviceId(), deviceInformation.getVersion(),
                deviceInformation.getUsername(), deviceInformation.getDepartmentCode());

        validatorPort.validateSupplierCode(deviceInformation.getUsername());

        logTerminalRequest(terminalRequest);

        logTerminalId(terminalRequest);

        logVersion(terminalRequest);

        final DeliveryReturnRequest request = requestMapper.map(terminalRequest);

        final DeliveryReturnResponse response = deliveryReturnPort.deliverReturn(request);

        return new ResponseEntity<>(responseMapper.map(response), HttpStatus.OK);
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

    @Override
    public boolean supports(final ProcessType processType) {
        return RETURN.equals(processType);
    }

    @Override
    public Response processRequest(final Request request) {
        return null;
    }
}
