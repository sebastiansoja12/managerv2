package com.warehouse.deliverymissed.infrastructure.adapter.primary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.deliverymissed.domain.vo.DeliveryMissedResponse;
import com.warehouse.deliverymissed.dto.DeliveryMissedResponseDto;

@Mapper
public interface DeliveryMissedResponseMapper {
    DeliveryMissedResponseDto map(final DeliveryMissedResponse response);
}
