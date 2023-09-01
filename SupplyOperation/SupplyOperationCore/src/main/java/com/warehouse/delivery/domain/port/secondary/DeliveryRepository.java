package com.warehouse.delivery.domain.port.secondary;

import com.warehouse.delivery.domain.model.Delivery;

public interface DeliveryRepository {
    Delivery saveDelivery(Delivery delivery);
}
