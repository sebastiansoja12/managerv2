package com.warehouse.delivery.infrastructure.adapter.primary;

import java.util.List;

import com.warehouse.delivery.infrastructure.adapter.primary.dto.DeliveryRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.delivery.domain.port.primary.DeliveryPort;
import com.warehouse.delivery.infrastructure.adapter.primary.mapper.DeliveryRequestMapper;
import com.warehouse.delivery.infrastructure.adapter.primary.mapper.DeliveryResponseMapper;

import lombok.AllArgsConstructor;

@RequestMapping("/deliveries")
@RestController
@AllArgsConstructor
public class DeliveryController {

    private final DeliveryPort deliveryPort;

    private final DeliveryRequestMapper requestMapper;

    private final DeliveryResponseMapper responseMapper;

    @PostMapping
    public ResponseEntity<?> createSupply(@RequestBody List<DeliveryRequestDto> deliveryRequest) {
        final List<DeliveryRequest> request = requestMapper.map(deliveryRequest);
        final List<DeliveryResponse> response = deliveryPort.deliver(request);
        return new ResponseEntity<>(responseMapper.map(response), HttpStatus.OK);
    }
}
