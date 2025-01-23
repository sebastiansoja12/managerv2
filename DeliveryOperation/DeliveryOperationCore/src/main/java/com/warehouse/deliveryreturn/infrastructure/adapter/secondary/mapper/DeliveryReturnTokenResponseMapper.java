package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;

import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ReturnTokenResponseDto;
import org.mapstruct.Mapper;

import com.warehouse.deliveryreturn.domain.vo.ReturnTokenResponse;

@Mapper
public interface DeliveryReturnTokenResponseMapper {
    ReturnTokenResponse map(final ReturnTokenResponseDto response);
}
