package com.warehouse.deliveryreject.infrastructure.adapter.primary.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse;
import com.warehouse.deliveryreject.dto.response.DeliveryRejectResponseDto;

@Mapper
public interface DeliveryRejectResponseMapper {
    List<DeliveryRejectResponseDto> map(final List<DeliveryRejectResponse> response);
}
