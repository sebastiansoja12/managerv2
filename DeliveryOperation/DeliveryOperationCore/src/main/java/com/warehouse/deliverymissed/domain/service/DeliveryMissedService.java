package com.warehouse.deliverymissed.domain.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliverymissed.domain.model.DeliveryMissedRequest;
import com.warehouse.deliverymissed.domain.vo.DeliveryAttemptNumber;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity.id.DeliveryMissedDetailId;

public interface DeliveryMissedService {
    DeliveryMissed saveDelivery(DeliveryMissedRequest request);
    DeliveryAttemptNumber countDeliveryAttempts(final ShipmentId shipmentId);

    DeliveryMissedDetailId nextId();
}
