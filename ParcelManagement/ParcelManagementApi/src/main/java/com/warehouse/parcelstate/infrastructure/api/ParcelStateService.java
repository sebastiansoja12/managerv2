package com.warehouse.parcelstate.infrastructure.api;


import com.warehouse.parcelstate.infrastructure.api.dto.DeliveryStateRequestDto;
import com.warehouse.parcelstate.infrastructure.api.dto.DeliveryStateResponseDto;

public interface ParcelStateService {

    DeliveryStateResponseDto updateStatus(DeliveryStateRequestDto request);
}
