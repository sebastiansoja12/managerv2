package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturn;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.entity.DeliveryReturnEntity;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper
public interface DeliveryReturnMapper {
    DeliveryReturn map(DeliveryReturnEntity deliveryReturnEntity);

    @Mapping(target = "created", expression = "java(getLocalDateTime())")
    DeliveryReturnEntity map(DeliveryReturnDetails deliveryReturnRequest);


    default LocalDateTime getLocalDateTime() {
        return LocalDateTime.now();
    }
}
