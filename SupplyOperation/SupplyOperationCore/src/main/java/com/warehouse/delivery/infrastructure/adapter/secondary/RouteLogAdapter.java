package com.warehouse.delivery.infrastructure.adapter.secondary;

import java.util.List;
import java.util.Set;

import com.warehouse.delivery.domain.model.DeliveryRouteRequest;
import com.warehouse.delivery.domain.model.DeliveryRouteResponse;
import com.warehouse.delivery.domain.port.secondary.RouteLogServicePort;
import com.warehouse.delivery.infrastructure.adapter.secondary.mapper.DeliveryMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RouteLogAdapter implements RouteLogServicePort {


    private final DeliveryMapper deliveryMapper;


    @Override
    public List<DeliveryRouteResponse> deliver(Set<DeliveryRouteRequest> deliveryRequest) {
        return deliveryRequest.stream()
                .map(deliveryMapper::map)
                .toList();
    }

}
