package com.warehouse.delivery.infrastructure.adapter.primary.processresolver;

import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.stereotype.Component;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.model.Request;
import com.warehouse.delivery.domain.model.Response;
import com.warehouse.delivery.infrastructure.adapter.primary.ProcessHandler;
import com.warehouse.delivery.infrastructure.adapter.primary.mapper.DeliveryRequestMapper;
import com.warehouse.delivery.infrastructure.adapter.primary.mapper.DeliveryResponseMapper;
import com.warehouse.deliveryreturn.infrastructure.api.DeliveryReturnService;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnRequestDto;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnResponseDto;

@Component
public class ProcessReturnHandler implements ProcessHandler {

    private final DeliveryReturnService deliveryReturnService;

    private final DeliveryRequestMapper requestMapper = getMapper(DeliveryRequestMapper.class);

    private final DeliveryResponseMapper responseMapper = getMapper(DeliveryResponseMapper.class);

    public ProcessReturnHandler(final DeliveryReturnService deliveryReturnService) {
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
