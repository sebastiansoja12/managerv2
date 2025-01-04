package com.warehouse.deliveryreject.infrastructure.adapter.primary;

import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.stereotype.Component;

import com.warehouse.deliveryreject.DeliveryRejectService;
import com.warehouse.deliveryreject.domain.model.DeliveryRejectRequest;
import com.warehouse.deliveryreject.domain.port.primary.DeliveryRejectPort;
import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse;
import com.warehouse.deliveryreject.dto.request.DeliveryRejectRequestDto;
import com.warehouse.deliveryreject.dto.response.DeliveryRejectResponseDto;
import com.warehouse.deliveryreject.infrastructure.adapter.primary.mapper.DeliveryRejectRequestMapper;
import com.warehouse.deliveryreject.infrastructure.adapter.primary.mapper.DeliveryRejectResponseMapper;

@Component
public class DeliveryRejectAdapter implements DeliveryRejectService {

    private final DeliveryRejectPort deliveryRejectPort;
    private final DeliveryRejectRequestMapper requestMapper = getMapper(DeliveryRejectRequestMapper.class);
    private final DeliveryRejectResponseMapper responseMapper = getMapper(DeliveryRejectResponseMapper.class);

    public DeliveryRejectAdapter(final DeliveryRejectPort deliveryRejectPort) {
        this.deliveryRejectPort = deliveryRejectPort;
    }

    @Override
    public DeliveryRejectResponseDto deliverReject(final DeliveryRejectRequestDto deliveryRejectRequest) {
        final DeliveryRejectRequest request = requestMapper.map(deliveryRejectRequest);
        final DeliveryRejectResponse response = this.deliveryRejectPort.deliverReject(request);
        return responseMapper.map(response);
    }
}
