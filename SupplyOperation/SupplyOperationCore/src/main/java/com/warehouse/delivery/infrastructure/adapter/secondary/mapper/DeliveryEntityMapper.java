package com.warehouse.delivery.infrastructure.adapter.secondary.mapper;

import com.warehouse.delivery.domain.model.Delivery;
import com.warehouse.delivery.infrastructure.adapter.secondary.entity.DeliveryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeliveryEntityMapper {
    DeliveryEntity map(Delivery delivery);

    Delivery map(DeliveryEntity entity);
}
