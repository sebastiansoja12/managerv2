package com.warehouse.logistics.infrastructure.adapter.primary.processresolver;

import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.logistics.infrastructure.adapter.primary.mapper.LogisticsRequestMapper;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.LogisticsResponseMapper;
import org.springframework.stereotype.Component;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.logistics.domain.model.Request;
import com.warehouse.logistics.domain.model.Response;
import com.warehouse.logistics.infrastructure.adapter.primary.ProcessHandler;
import com.warehouse.deliveryreject.DeliveryRejectService;
import com.warehouse.deliveryreject.dto.request.DeliveryRejectRequestDto;
import com.warehouse.deliveryreject.dto.response.DeliveryRejectResponseDto;

@Component
public class ProcessRejectResolver implements ProcessHandler {

    private final DeliveryRejectService deliveryRejectService;

    private final LogisticsRequestMapper requestMapper = getMapper(LogisticsRequestMapper.class);

    private final LogisticsResponseMapper responseMapper = getMapper(LogisticsResponseMapper.class);

    public ProcessRejectResolver(final DeliveryRejectService deliveryRejectService) {
        this.deliveryRejectService = deliveryRejectService;
    }

    @Override
    public boolean supports(final ProcessType processType) {
        // return REJECT.equals(processType);
        return false;
    }

    @Override
    public Response processRequest(final Request request) {
        final DeliveryRejectRequestDto deliveryRejectRequest = requestMapper.mapToDeliveryRejectRequest(request);
        final DeliveryRejectResponseDto deliveryRejectResponse =
                deliveryRejectService.processDeliveryReject(deliveryRejectRequest);
        return responseMapper.mapDeliveryRejectResponse(deliveryRejectResponse);
    }
}
