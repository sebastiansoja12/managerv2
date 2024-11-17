package com.warehouse.delivery.domain.port.primary;

import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;

import java.util.List;
import java.util.Set;

public interface DeliveryPort {

    Set<DeliveryResponse> processDelivery(List<DeliveryRequest> deliveryRequest);
}
