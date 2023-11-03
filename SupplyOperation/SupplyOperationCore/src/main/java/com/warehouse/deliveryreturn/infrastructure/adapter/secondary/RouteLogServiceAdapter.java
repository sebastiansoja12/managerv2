package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import com.warehouse.delivery.domain.model.DeliveryRouteRequest;
import com.warehouse.delivery.domain.model.DeliveryRouteResponse;
import com.warehouse.deliveryreturn.domain.port.secondary.RouteLogServicePort;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class RouteLogServiceAdapter implements RouteLogServicePort {
    @Override
    public List<DeliveryRouteResponse> logDeliverReturn(Set<DeliveryRouteRequest> deliveryRequest) {
        return null;
    }
}
