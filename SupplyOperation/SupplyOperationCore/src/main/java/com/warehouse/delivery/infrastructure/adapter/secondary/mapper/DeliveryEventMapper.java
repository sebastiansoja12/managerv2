package com.warehouse.delivery.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.delivery.domain.model.Delivery;
import com.warehouse.routelogger.dto.DeliveryRequestDto;

@Mapper
public interface DeliveryEventMapper {

    DeliveryRequestDto map(Delivery delivery);
}
