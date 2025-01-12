package com.warehouse.deliverymissed.domain.port.secondary;

import com.warehouse.deliverymissed.domain.model.DeliveryMissedRequest;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;

public interface DeliveryMissedRepository {
    DeliveryMissed saveDeliveryMissed(DeliveryMissedRequest request);
}
