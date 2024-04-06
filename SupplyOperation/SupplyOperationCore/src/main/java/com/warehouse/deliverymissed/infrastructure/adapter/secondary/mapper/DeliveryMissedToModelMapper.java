package com.warehouse.deliverymissed.infrastructure.adapter.secondary.mapper;

import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity.DeliveryMissedEntity;
import org.mapstruct.Mapper;

@Mapper
public interface DeliveryMissedToModelMapper {
    DeliveryMissed map(DeliveryMissedEntity deliveryMissedEntity);
}
