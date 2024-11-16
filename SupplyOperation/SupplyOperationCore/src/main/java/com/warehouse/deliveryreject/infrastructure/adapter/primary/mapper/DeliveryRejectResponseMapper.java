package com.warehouse.deliveryreject.infrastructure.adapter.primary.mapper;

import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse;
import com.warehouse.deliveryreject.dto.response.DeliveryRejectResponseDto;
import org.mapstruct.Mapper;

@Mapper
public interface DeliveryRejectResponseMapper {
    DeliveryRejectResponseDto map(final DeliveryRejectResponse response);
}
