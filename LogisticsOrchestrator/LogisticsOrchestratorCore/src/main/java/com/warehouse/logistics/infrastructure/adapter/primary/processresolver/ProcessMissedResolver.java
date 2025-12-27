package com.warehouse.logistics.infrastructure.adapter.primary.processresolver;

import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.stereotype.Component;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.logistics.domain.model.Request;
import com.warehouse.logistics.domain.model.Response;
import com.warehouse.logistics.infrastructure.adapter.primary.ProcessHandler;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.LogisticsRequestMapper;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.LogisticsResponseMapper;
import com.warehouse.deliverymissed.DeliveryMissedService;
import com.warehouse.deliverymissed.dto.DeliveryMissedRequestDto;
import com.warehouse.deliverymissed.dto.DeliveryMissedResponseDto;

@Component
public class ProcessMissedResolver implements ProcessHandler {

    private final DeliveryMissedService deliveryMissedService;

    private final LogisticsRequestMapper requestMapper = getMapper(LogisticsRequestMapper.class);

    private final LogisticsResponseMapper responseMapper = getMapper(LogisticsResponseMapper.class);

    public ProcessMissedResolver(final DeliveryMissedService deliveryMissedService) {
        this.deliveryMissedService = deliveryMissedService;
    }

    @Override
    public boolean supports(final ProcessType processType) {
        return ProcessType.MISS.equals(processType);
    }

    @Override
    public Response processRequest(final Request request) {
        final DeliveryMissedRequestDto deliveryMissedRequest = requestMapper.mapToDeliveryMissedRequest(request);
        final DeliveryMissedResponseDto deliveryMissedResponse =
                deliveryMissedService.processDeliveryMiss(deliveryMissedRequest);
        return responseMapper.mapDeliveryMissedResponse(deliveryMissedResponse);
    }
}
