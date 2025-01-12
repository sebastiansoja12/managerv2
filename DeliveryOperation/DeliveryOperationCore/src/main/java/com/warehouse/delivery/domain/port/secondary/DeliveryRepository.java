package com.warehouse.delivery.domain.port.secondary;

import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;

public interface DeliveryRepository {
    DeliveryResponse create(final DeliveryRequest request);
}
