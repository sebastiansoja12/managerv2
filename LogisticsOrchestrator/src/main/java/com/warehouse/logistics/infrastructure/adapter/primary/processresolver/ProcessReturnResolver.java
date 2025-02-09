package com.warehouse.logistics.infrastructure.adapter.primary.processresolver;

import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.logistics.infrastructure.adapter.primary.mapper.LogisticsRequestMapper;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.LogisticsResponseMapper;
import org.springframework.stereotype.Component;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.logistics.domain.model.Request;
import com.warehouse.logistics.domain.model.Response;
import com.warehouse.logistics.infrastructure.adapter.primary.ProcessHandler;
import com.warehouse.deliveryreturn.infrastructure.api.DeliveryReturnService;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnRequestDto;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnResponseDto;

@Component
public class ProcessReturnResolver implements ProcessHandler {

    private final DeliveryReturnService deliveryReturnService;

    private final LogisticsRequestMapper requestMapper = getMapper(LogisticsRequestMapper.class);

    private final LogisticsResponseMapper responseMapper = getMapper(LogisticsResponseMapper.class);

    public ProcessReturnResolver(final DeliveryReturnService deliveryReturnService) {
        this.deliveryReturnService = deliveryReturnService;
    }

    @Override
    public boolean supports(final ProcessType processType) {
        return ProcessType.RETURN.equals(processType);
    }

    @Override
    public Response processRequest(final Request request) {
        final DeliveryReturnRequestDto deliveryReturnRequest = requestMapper.mapToDeliveryReturnRequest(request);
		final DeliveryReturnResponseDto deliveryReturnResponse = deliveryReturnService
				.processDeliveryReturn(deliveryReturnRequest);
        return responseMapper.mapDeliveryReturnResponse(deliveryReturnResponse);
    }
}
