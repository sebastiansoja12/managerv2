package com.warehouse.deliverymissed.infrastructure.adapter.secondary.mapper;

import com.warehouse.routelogger.dto.DeliveryRequestDto;
import org.mapstruct.Mapper;

import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;

@Mapper
public interface DeliveryMissedEventMapper {

    DeliveryRequestDto map(DeliveryMissed deliveryMissed);
}
