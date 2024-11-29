package com.warehouse.deliveryreturn.infrastructure.adapter.primary;


import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.stereotype.Component;

import com.warehouse.deliveryreturn.domain.model.DeliveryReturnRequest;
import com.warehouse.deliveryreturn.domain.port.primary.DeliveryReturnPort;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponse;
import com.warehouse.deliveryreturn.infrastructure.adapter.primary.mapper.DeliveryReturnRequestMapper;
import com.warehouse.deliveryreturn.infrastructure.adapter.primary.mapper.DeliveryReturnResponseMapper;
import com.warehouse.deliveryreturn.infrastructure.api.DeliveryReturnService;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnRequestDto;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnResponseDto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DeliveryReturnAdapter implements DeliveryReturnService {

    private final DeliveryReturnPort deliveryReturnPort;

    private final DeliveryReturnRequestMapper requestMapper = getMapper(DeliveryReturnRequestMapper.class);

    private final DeliveryReturnResponseMapper responseMapper = getMapper(DeliveryReturnResponseMapper.class);

    public DeliveryReturnAdapter(final DeliveryReturnPort deliveryReturnPort) {
        this.deliveryReturnPort = deliveryReturnPort;
    }

    @Override
    public DeliveryReturnResponseDto processDeliveryReturn(final DeliveryReturnRequestDto deliveryReturnRequest) {
        final DeliveryReturnRequest request = requestMapper.mapToDeliveryReturnRequest(deliveryReturnRequest);
        final DeliveryReturnResponse response = deliveryReturnPort.deliverReturn(request);
        return responseMapper.map(response);
    }
}
