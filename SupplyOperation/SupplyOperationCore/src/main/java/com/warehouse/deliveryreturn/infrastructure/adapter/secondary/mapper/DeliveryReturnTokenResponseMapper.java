package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;

import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnSignature;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.TokenDto;
import org.mapstruct.Mapper;

@Mapper
public interface DeliveryReturnTokenResponseMapper {
    DeliveryReturnSignature map(TokenDto token);
}
