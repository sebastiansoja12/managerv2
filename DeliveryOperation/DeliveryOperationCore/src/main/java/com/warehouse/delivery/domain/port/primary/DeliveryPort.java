package com.warehouse.delivery.domain.port.primary;

import java.util.Set;

import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;

public interface DeliveryPort {

    Set<DeliveryResponse> processDelivery(final Set<DeliveryRequest> deliveryRequest);
}
