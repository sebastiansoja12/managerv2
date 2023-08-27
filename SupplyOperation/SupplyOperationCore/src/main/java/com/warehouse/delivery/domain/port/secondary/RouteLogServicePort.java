package com.warehouse.delivery.domain.port.secondary;

import java.util.List;
import java.util.Set;

import com.warehouse.delivery.domain.model.DeliveryRouteRequest;
import com.warehouse.delivery.domain.model.DeliveryRouteResponse;

public interface RouteLogServicePort {

    List<DeliveryRouteResponse> deliver(Set<DeliveryRouteRequest> deliveryRequest);
}
