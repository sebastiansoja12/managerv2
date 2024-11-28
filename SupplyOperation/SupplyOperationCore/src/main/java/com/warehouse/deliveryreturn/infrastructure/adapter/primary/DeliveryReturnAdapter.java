package com.warehouse.deliveryreturn.infrastructure.adapter.primary;


import static com.warehouse.commonassets.enumeration.ProcessType.RETURN;
import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.stereotype.Component;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.model.Request;
import com.warehouse.delivery.domain.model.Response;
import com.warehouse.delivery.infrastructure.adapter.primary.ProcessHandler;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnRequest;
import com.warehouse.deliveryreturn.domain.port.primary.DeliveryReturnPort;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponse;
import com.warehouse.deliveryreturn.infrastructure.adapter.primary.mapper.DeliveryReturnRequestMapper;
import com.warehouse.deliveryreturn.infrastructure.adapter.primary.mapper.DeliveryReturnResponseMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DeliveryReturnAdapter implements ProcessHandler {

    private final DeliveryReturnPort deliveryReturnPort;

    private final DeliveryReturnRequestMapper requestMapper = getMapper(DeliveryReturnRequestMapper.class);

    private final DeliveryReturnResponseMapper responseMapper = getMapper(DeliveryReturnResponseMapper.class);

    public DeliveryReturnAdapter(final DeliveryReturnPort deliveryReturnPort) {
        this.deliveryReturnPort = deliveryReturnPort;
    }

    @Override
    public boolean supports(final ProcessType processType) {
        return RETURN.equals(processType);
    }

    @Override
    public Response processRequest(final Request request) {
        final DeliveryReturnRequest returnRequest = requestMapper.map(request);
        final DeliveryReturnResponse returnResponse = deliveryReturnPort.deliverReturn(returnRequest);
        return responseMapper.map(returnResponse);
    }
}
