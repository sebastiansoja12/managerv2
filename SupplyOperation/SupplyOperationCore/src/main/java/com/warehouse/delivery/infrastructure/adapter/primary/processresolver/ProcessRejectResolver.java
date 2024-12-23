package com.warehouse.delivery.infrastructure.adapter.primary.processresolver;

import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.stereotype.Component;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.model.Request;
import com.warehouse.delivery.domain.model.Response;
import com.warehouse.delivery.infrastructure.adapter.primary.ProcessHandler;
import com.warehouse.delivery.infrastructure.adapter.primary.mapper.DeliveryRequestMapper;
import com.warehouse.delivery.infrastructure.adapter.primary.mapper.DeliveryResponseMapper;
import com.warehouse.deliveryreject.DeliveryRejectService;
import com.warehouse.deliveryreject.dto.request.DeliveryRejectRequestDto;
import com.warehouse.deliveryreject.dto.response.DeliveryRejectResponseDto;

@Component
public class ProcessRejectResolver implements ProcessHandler {

    private final DeliveryRejectService deliveryRejectService;

    private final DeliveryRequestMapper requestMapper = getMapper(DeliveryRequestMapper.class);

    private final DeliveryResponseMapper responseMapper = getMapper(DeliveryResponseMapper.class);

    public ProcessRejectResolver(final DeliveryRejectService deliveryRejectService) {
        this.deliveryRejectService = deliveryRejectService;
    }

    @Override
    public boolean supports(final ProcessType processType) {
        return ProcessType.REJECT.equals(processType);
    }

    @Override
    public Response processRequest(final Request request) {
        final DeliveryRejectRequestDto deliveryRejectRequest = requestMapper.mapToDeliveryRejectRequest(request);
        final DeliveryRejectResponseDto deliveryRejectResponse =
                deliveryRejectService.reportDeliveryRejection(deliveryRejectRequest);
        return responseMapper.mapDeliveryRejectResponse(deliveryRejectResponse);
    }
}
