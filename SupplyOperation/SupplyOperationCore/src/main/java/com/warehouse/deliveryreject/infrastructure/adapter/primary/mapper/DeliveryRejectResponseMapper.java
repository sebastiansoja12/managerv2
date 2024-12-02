package com.warehouse.deliveryreject.infrastructure.adapter.primary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse;
import com.warehouse.deliveryreject.dto.response.DeliveryRejectResponseDto;
import org.mapstruct.Mapping;

@Mapper
public interface DeliveryRejectResponseMapper {
    @Mapping(target = "deviceInformation.version.value", source = "deviceInformation.version")
    @Mapping(target = "deviceInformation.username.value", source = "deviceInformation.username")
    DeliveryRejectResponseDto map(final DeliveryRejectResponse response);
}
