package com.warehouse.deliveryreturn.infrastructure.adapter.primary.mapper;


import org.mapstruct.Mapper;

import com.warehouse.delivery.domain.model.Response;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponse;

@Mapper
public interface DeliveryReturnResponseMapper {
    Response map(final DeliveryReturnResponse response);
}
