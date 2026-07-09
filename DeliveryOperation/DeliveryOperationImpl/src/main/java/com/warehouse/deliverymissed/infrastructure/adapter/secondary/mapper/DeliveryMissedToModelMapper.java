package com.warehouse.deliverymissed.infrastructure.adapter.secondary.mapper;

import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity.DeliveryMissedEntity;


public interface DeliveryMissedToModelMapper {
    DeliveryMissed map(DeliveryMissedEntity deliveryMissedEntity);
}
