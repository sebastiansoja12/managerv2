package com.warehouse.deliverymissed.domain.service;

import com.warehouse.deliverymissed.domain.model.DeliveryMissedRequest;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;

public interface DeliveryMissedService {
    DeliveryMissed saveDelivery(DeliveryMissedRequest request);
}
