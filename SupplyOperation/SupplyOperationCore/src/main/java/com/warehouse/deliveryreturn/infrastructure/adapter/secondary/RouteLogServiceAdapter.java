package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestGatewaySupport;

import com.warehouse.deliveryreturn.domain.port.secondary.RouteLogServicePort;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.DeliveryReturnRouteRequest;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.DeliveryReturnRouteRequestDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper.DeliveryRouteRequestMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class RouteLogServiceAdapter extends RestGatewaySupport implements RouteLogServicePort {

    private final RestClient restClient;

    private final DeliveryRouteRequestMapper requestMapper = getMapper(DeliveryRouteRequestMapper.class);

    @Override
    public void logDeliverReturn(DeliveryReturnRouteRequest deliveryReturnRouteRequest) {
        final DeliveryReturnRouteRequestDto request = requestMapper.map(deliveryReturnRouteRequest);

    }
}
