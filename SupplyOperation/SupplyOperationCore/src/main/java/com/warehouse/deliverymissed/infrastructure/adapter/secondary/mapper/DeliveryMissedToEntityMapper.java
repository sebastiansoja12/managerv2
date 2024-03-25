package com.warehouse.deliverymissed.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.deliverymissed.domain.model.DeliveryMissedRequest;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity.DeliveryMissedEntity;

@Mapper
public interface DeliveryMissedToEntityMapper {

    DeliveryMissedEntity map(DeliveryMissedRequest deliveryMissedRequest);
}
