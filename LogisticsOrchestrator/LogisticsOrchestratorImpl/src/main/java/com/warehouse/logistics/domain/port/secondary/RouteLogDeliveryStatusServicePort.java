package com.warehouse.logistics.domain.port.secondary;

import java.util.Set;

import com.warehouse.logistics.domain.model.Delivery;

public interface RouteLogDeliveryStatusServicePort {

    void deliver(Set<Delivery> deliveryRequest);
}
