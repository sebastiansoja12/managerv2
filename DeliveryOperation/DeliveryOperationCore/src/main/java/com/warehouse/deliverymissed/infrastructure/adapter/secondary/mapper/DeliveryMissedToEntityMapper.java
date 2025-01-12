package com.warehouse.deliverymissed.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.deliverymissed.domain.model.DeliveryMissedRequest;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity.DeliveryMissedEntity;
import org.mapstruct.Mapping;

@Mapper
public interface DeliveryMissedToEntityMapper {

    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "token", constant = "token")
    DeliveryMissedEntity map(DeliveryMissedRequest deliveryMissedRequest);
}
