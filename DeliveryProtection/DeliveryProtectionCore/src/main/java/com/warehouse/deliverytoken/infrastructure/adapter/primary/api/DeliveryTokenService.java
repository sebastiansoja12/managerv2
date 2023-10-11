package com.warehouse.deliverytoken.infrastructure.adapter.primary.api;

import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.DeliveryTokenRequestDto;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.DeliveryTokenResponseDto;

public interface DeliveryTokenService {
    DeliveryTokenResponseDto protect(DeliveryTokenRequestDto request);
}
