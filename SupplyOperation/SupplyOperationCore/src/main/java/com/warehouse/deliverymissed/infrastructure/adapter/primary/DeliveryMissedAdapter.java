package com.warehouse.deliverymissed.infrastructure.adapter.primary;


import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

@RestController
@RequestMapping("/delivery-missings")
@RequiredArgsConstructor
@Slf4j
public class DeliveryMissedAdapter {

    private final DeliveryMissedRequestMapper requestMapper = getMapper(DeliveryMissedRequestMapper.class);

    private final DeliveryMissedResponseMapper responseMapper = getMapper(DeliveryMissedResponseMapper.class);

    private final DeliveryMissedPort deliveryMissedPort;

    private final SupplierValidatorPort validatorPort;

    @PostMapping(produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> deliveryReturn(@RequestBody TerminalRequest terminalRequest) {

        final TerminalDeviceInformation terminalDeviceInformation = terminalRequest.getTerminalDeviceInformation();

        log.info("Detected request from Terminal device: ID - {}, Version - {}, Responsible User - {}, Depot - {}",
                terminalDeviceInformation.getTerminalId(), terminalDeviceInformation.getVersion(),
                terminalDeviceInformation.getUsername(), terminalDeviceInformation.getDepotCode());

        validatorPort.validateSupplierCode(terminalDeviceInformation.getUsername());

        final DeliveryMissedRequest request = requestMapper.map(terminalRequest);

        final DeliveryMissedResponse response = deliveryMissedPort.logMissedDelivery(request);

        return new ResponseEntity<>(responseMapper.map(response), HttpStatus.OK);
    }
}
