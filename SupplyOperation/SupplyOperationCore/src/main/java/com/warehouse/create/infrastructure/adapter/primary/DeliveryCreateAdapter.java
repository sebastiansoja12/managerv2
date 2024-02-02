package com.warehouse.create.infrastructure.adapter.primary;


import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.warehouse.deliveryreturn.infrastructure.adapter.primary.mapper.DeliveryReturnRequestMapper;
import com.warehouse.deliveryreturn.infrastructure.adapter.primary.mapper.DeliveryReturnResponseMapper;

@Endpoint
public class DeliveryCreateAdapter extends DeliveryCreateAdapterConfig {

    private final DeliveryReturnRequestMapper requestMapper = getMapper(DeliveryReturnRequestMapper.class);

    private final DeliveryReturnResponseMapper responseMapper = getMapper(DeliveryReturnResponseMapper.class);

    private static final String NAMESPACE_URI = "delivery-create-service";

    // TODO
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "")
    @ResponsePayload
    public ResponseEntity<?> create() {
        return null;
    }

}
