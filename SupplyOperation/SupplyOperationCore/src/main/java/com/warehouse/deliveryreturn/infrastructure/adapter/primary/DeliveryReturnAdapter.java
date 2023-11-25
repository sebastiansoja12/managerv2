package com.warehouse.deliveryreturn.infrastructure.adapter.primary;


import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.deliveryreturn.infrastructure.api.zebradevice.ZebraDeviceInformation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.deliveryreturn.domain.model.DeliveryReturnRequest;
import com.warehouse.deliveryreturn.domain.port.primary.DeliveryReturnPort;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponse;
import com.warehouse.deliveryreturn.infrastructure.adapter.primary.mapper.DeliveryReturnRequestMapper;
import com.warehouse.deliveryreturn.infrastructure.adapter.primary.mapper.DeliveryReturnResponseMapper;
import com.warehouse.deliveryreturn.infrastructure.api.dto.request.ZebraDeliveryReturnRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/delivery-returns")
@RequiredArgsConstructor
@Slf4j
public class DeliveryReturnAdapter {


    private final DeliveryReturnPort deliveryReturnPort;

    private final DeliveryReturnRequestMapper requestMapper = getMapper(DeliveryReturnRequestMapper.class);

    private final DeliveryReturnResponseMapper responseMapper = getMapper(DeliveryReturnResponseMapper.class);

    @PostMapping(produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> deliveryReturn(@RequestBody ZebraDeliveryReturnRequest zebraDeliveryReturnRequest) {

        final ZebraDeviceInformation zebraDeviceInformation = zebraDeliveryReturnRequest.getZebraDeviceInformation();

        log.info("Detected request from Zebra device: ID - {}, Version - {}, Responsible User - {}, Depot - {}",
                zebraDeviceInformation.getZebraId(), zebraDeviceInformation.getVersion(),
                zebraDeviceInformation.getUsername(), zebraDeviceInformation.getDepotCode());

        final DeliveryReturnRequest request = requestMapper.map(zebraDeliveryReturnRequest);

        final DeliveryReturnResponse response = deliveryReturnPort.deliverReturn(request);

        return new ResponseEntity<>(responseMapper.map(response), HttpStatus.OK);
    }

}
