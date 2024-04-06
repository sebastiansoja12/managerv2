package com.warehouse.routelogger.infrastructure.adapter.secondary.mapper;


import org.mapstruct.Mapper;

import com.warehouse.routelogger.domain.model.AnyDeliveryRequest;
import com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto.DeliveryRequestDto;

@Mapper
public interface RouteLogRequestMapper {

    DeliveryRequestDto map(AnyDeliveryRequest anyDeliveryRequest);
}
