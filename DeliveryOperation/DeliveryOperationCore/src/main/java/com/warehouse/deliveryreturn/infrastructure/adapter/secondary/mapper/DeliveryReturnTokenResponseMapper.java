package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliveryreturn.domain.vo.ReturnTokenResponse;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ReturnTokenResponseDto;
import com.warehouse.deliveryreturn.infrastructure.api.dto.ShipmentIdDto;

@Mapper
public interface DeliveryReturnTokenResponseMapper {
    ReturnTokenResponse map(final ReturnTokenResponseDto response);

    ShipmentId map(ShipmentIdDto value);
}
