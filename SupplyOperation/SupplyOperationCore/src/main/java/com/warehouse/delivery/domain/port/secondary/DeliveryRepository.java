package com.warehouse.delivery.domain.port.secondary;

import com.warehouse.delivery.domain.model.Delivery;
import com.warehouse.delivery.domain.model.DeliveryRequest;

public interface DeliveryRepository {
    Delivery saveDelivery(DeliveryRequest request);
}
