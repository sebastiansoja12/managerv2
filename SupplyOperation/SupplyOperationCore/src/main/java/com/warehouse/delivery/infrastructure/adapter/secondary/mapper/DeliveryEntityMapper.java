package com.warehouse.delivery.infrastructure.adapter.secondary.mapper;

import com.warehouse.delivery.domain.model.DeliveryRequest;
import org.mapstruct.Mapper;

import com.warehouse.delivery.domain.model.Delivery;
import com.warehouse.delivery.infrastructure.adapter.secondary.entity.DeliveryEntity;

@Mapper
public interface DeliveryEntityMapper {
    DeliveryEntity map(DeliveryRequest delivery);

    Delivery map(DeliveryEntity entity);
}
