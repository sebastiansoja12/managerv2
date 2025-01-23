package com.warehouse.delivery.domain.port.secondary;

import java.util.Set;

import com.warehouse.delivery.domain.model.Delivery;

public interface RouteLogDeliveryStatusServicePort {

    void deliver(Set<Delivery> deliveryRequest);
}
