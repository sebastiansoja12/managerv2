package com.warehouse.logistics.domain.port.primary;

import java.util.Set;

import com.warehouse.logistics.domain.model.DeliveryRequest;
import com.warehouse.logistics.domain.model.DeliveryResponse;

public interface DeliveryPort {

    Set<DeliveryResponse> processDelivery(final Set<DeliveryRequest> deliveryRequest);
}
