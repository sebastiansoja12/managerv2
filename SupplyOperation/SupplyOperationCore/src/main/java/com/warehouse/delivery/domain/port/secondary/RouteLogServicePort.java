package com.warehouse.delivery.domain.port.secondary;

import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;

import java.util.List;
import java.util.Set;

public interface RouteLogServicePort {

    List<DeliveryResponse> deliver(Set<DeliveryRequest> deliveryRequest);
}
