package com.warehouse.deliveryreturn.domain.port.secondary;

import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.DeliveryReturnRouteRequest;

public interface RouteLogServicePort {
    void logDeliverReturn(DeliveryReturnRouteRequest request);

}
