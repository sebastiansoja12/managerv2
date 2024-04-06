package com.warehouse.deliverymissed.domain.port.primary;

import com.warehouse.deliverymissed.domain.model.DeliveryMissedRequest;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedResponse;

public interface DeliveryMissedPort {
    DeliveryMissedResponse logMissedDelivery(DeliveryMissedRequest request);
}
