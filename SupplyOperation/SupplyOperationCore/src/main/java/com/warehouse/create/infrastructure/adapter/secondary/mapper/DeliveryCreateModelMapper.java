package com.warehouse.create.infrastructure.adapter.secondary.mapper;

import com.warehouse.create.domain.model.DeliveryCreate;
import com.warehouse.create.infrastructure.adapter.secondary.entity.DeliveryCreateEntity;
import org.mapstruct.Mapper;

@Mapper
public interface DeliveryCreateModelMapper {
    DeliveryCreate map(DeliveryCreateEntity deliveryCreate);
}
