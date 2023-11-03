package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturn;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.entity.DeliveryReturnEntity;

@Mapper
public interface DeliveryReturnMapper {
    DeliveryReturn map(DeliveryReturnEntity deliveryReturnEntity);

    DeliveryReturnEntity map(DeliveryReturnDetails deliveryReturnRequest);
}
