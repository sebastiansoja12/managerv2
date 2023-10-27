package com.warehouse.delivery.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.delivery.domain.model.Delivery;
import com.warehouse.delivery.infrastructure.adapter.secondary.entity.DeliveryEntity;

@Mapper
public interface DeliveryEntityMapper {
    DeliveryEntity map(Delivery delivery);

    Delivery map(DeliveryEntity entity);
}
