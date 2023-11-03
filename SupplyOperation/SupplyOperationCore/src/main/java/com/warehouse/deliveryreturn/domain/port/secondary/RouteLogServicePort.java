package com.warehouse.deliveryreturn.domain.port.secondary;

import com.warehouse.delivery.domain.model.DeliveryRouteRequest;
import com.warehouse.delivery.domain.model.DeliveryRouteResponse;

import java.util.List;
import java.util.Set;

public interface RouteLogServicePort {
    List<DeliveryRouteResponse> logDeliverReturn(Set<DeliveryRouteRequest> deliveryRequest);

}
