package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;

import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.DeliveryReturnRouteRequest;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.DeliveryReturnRouteRequestDto;
import org.mapstruct.Mapper;

@Mapper
public interface DeliveryRouteRequestMapper {
    DeliveryReturnRouteRequestDto map(DeliveryReturnRouteRequest request);
}
