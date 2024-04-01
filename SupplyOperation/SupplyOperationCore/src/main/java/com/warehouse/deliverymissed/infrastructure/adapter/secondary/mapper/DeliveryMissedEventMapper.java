package com.warehouse.deliverymissed.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.routelogger.dto.DeliveryMissedRequestDto;

@Mapper
public interface DeliveryMissedEventMapper {

    DeliveryMissedRequestDto map(DeliveryMissed deliveryMissed);
}
