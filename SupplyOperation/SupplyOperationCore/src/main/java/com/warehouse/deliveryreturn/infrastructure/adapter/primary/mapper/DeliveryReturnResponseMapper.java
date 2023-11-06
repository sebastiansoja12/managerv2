package com.warehouse.deliveryreturn.infrastructure.adapter.primary.mapper;


import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponse;
import com.warehouse.deliveryreturn.infrastructure.api.response.ZebraDeliveryReturnResponse;
import org.mapstruct.Mapper;

@Mapper
public interface DeliveryReturnResponseMapper {
    ZebraDeliveryReturnResponse map(DeliveryReturnResponse response);
}
