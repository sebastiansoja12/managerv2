package com.warehouse.deliveryreject.infrastructure.adapter.primary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.List;

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
    public List<DeliveryRejectResponseDto> reportDeliveryRejection(final List<DeliveryRejectRequestDto> deliveryRejectRequest) {
        final List<DeliveryRejectRequest> request = requestMapper.map(deliveryRejectRequest);
        final List<DeliveryRejectResponse> response = this.deliveryRejectPort.registerDeliveryRejection(request);
        return responseMapper.map(response);
    }
}
