package com.warehouse.delivery.domain.port.secondary;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.delivery.domain.model.DeliveryRequest;

public interface DeliveryRepository {
    DeliveryId create(final DeliveryRequest request);
}
