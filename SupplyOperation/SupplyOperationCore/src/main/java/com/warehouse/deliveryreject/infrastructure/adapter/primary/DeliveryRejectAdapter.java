package com.warehouse.deliveryreject.infrastructure.adapter.primary;

import static com.warehouse.commonassets.enumeration.ProcessType.REJECT;
import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.model.Request;
import com.warehouse.delivery.domain.model.Response;
import com.warehouse.delivery.infrastructure.adapter.primary.ProcessHandler;
import com.warehouse.deliveryreject.DeliveryRejectService;
import com.warehouse.deliveryreject.domain.model.DeliveryRejectRequest;
import com.warehouse.deliveryreject.domain.port.primary.DeliveryRejectPort;
import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse;
import com.warehouse.deliveryreject.dto.request.DeliveryRejectRequestDto;
import com.warehouse.deliveryreject.dto.response.DeliveryRejectResponseDto;
import com.warehouse.deliveryreject.infrastructure.adapter.primary.mapper.DeliveryRejectRequestMapper;
import com.warehouse.deliveryreject.infrastructure.adapter.primary.mapper.DeliveryRejectResponseMapper;

public class DeliveryRejectAdapter implements DeliveryRejectService, ProcessHandler {

    private final DeliveryRejectPort deliveryRejectPort;
    private final DeliveryRejectRequestMapper requestMapper = getMapper(DeliveryRejectRequestMapper.class);
    private final DeliveryRejectResponseMapper responseMapper = getMapper(DeliveryRejectResponseMapper.class);

    public DeliveryRejectAdapter(final DeliveryRejectPort deliveryRejectPort) {
        this.deliveryRejectPort = deliveryRejectPort;
    }

    @Override
    public DeliveryRejectResponseDto reportDeliveryRejection(final DeliveryRejectRequestDto deliveryRejectRequest) {
        final DeliveryRejectRequest request = this.requestMapper.map(deliveryRejectRequest);
        final DeliveryRejectResponse response = this.deliveryRejectPort.registerDeliveryRejection(request);
        return this.responseMapper.map(response);
    }

    @Override
    public boolean supports(final ProcessType processType) {
        return REJECT.equals(processType);
    }

    @Override
    public Response processRequest(final Request request) {
        final DeliveryRejectRequest deliveryRequest = null;
        final DeliveryRejectResponse deliveryRejectResponse = this.deliveryRejectPort.registerDeliveryRejection(deliveryRequest);
        return null;
    }
}
