package com.warehouse.deliverymissed.infrastructure.adapter.primary;


import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.stereotype.Component;

import com.warehouse.deliverymissed.DeliveryMissedService;
import com.warehouse.deliverymissed.domain.port.primary.DeliveryMissedPort;
import com.warehouse.deliverymissed.domain.model.DeliveryMissedRequest;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedResponse;
import com.warehouse.deliverymissed.dto.DeliveryMissedRequestDto;
import com.warehouse.deliverymissed.dto.DeliveryMissedResponseDto;
import com.warehouse.deliverymissed.infrastructure.adapter.primary.mapper.DeliveryMissedRequestMapper;
import com.warehouse.deliverymissed.infrastructure.adapter.primary.mapper.DeliveryMissedResponseMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DeliveryMissedAdapter implements DeliveryMissedService {

    private final DeliveryMissedRequestMapper requestMapper = getMapper(DeliveryMissedRequestMapper.class);

    private final DeliveryMissedResponseMapper responseMapper = getMapper(DeliveryMissedResponseMapper.class);

    private final DeliveryMissedPort deliveryMissedPort;

    public DeliveryMissedAdapter(final DeliveryMissedPort deliveryMissedPort) {
        this.deliveryMissedPort = deliveryMissedPort;
    }

    @Override
    public DeliveryMissedResponseDto processDeliveryMiss(final DeliveryMissedRequestDto deliveryMissedRequest) {
        final DeliveryMissedRequest request = requestMapper.map(deliveryMissedRequest);
        final DeliveryMissedResponse response = deliveryMissedPort.process(request);
        return responseMapper.map(response);
    }
}
