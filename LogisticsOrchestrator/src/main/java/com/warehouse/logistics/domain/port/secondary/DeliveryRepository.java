package com.warehouse.logistics.domain.port.secondary;

import com.warehouse.logistics.domain.model.DeliveryRequest;
import com.warehouse.logistics.domain.model.DeliveryResponse;

public interface DeliveryRepository {
    DeliveryResponse create(final DeliveryRequest request);
}
