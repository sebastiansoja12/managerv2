package com.warehouse.parcelstate.infrastructure.adapter.primary;

import com.warehouse.parcelstate.domain.port.primary.ParcelStatePort;

import com.warehouse.parcelstate.infrastructure.adapter.primary.mapper.ParcelStateRequestMapper;
import com.warehouse.parcelstate.infrastructure.adapter.primary.mapper.ParcelStateResponseMapper;
import com.warehouse.parcelstate.infrastructure.api.ParcelStateService;
import com.warehouse.parcelstate.infrastructure.api.dto.DeliveryStateRequestDto;
import com.warehouse.parcelstate.infrastructure.api.dto.DeliveryStateResponseDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ParcelStateServiceAdapter implements ParcelStateService {

    private final ParcelStatePort parcelStatePort;

    private final ParcelStateRequestMapper parcelStateRequestMapper;

    private final ParcelStateResponseMapper parcelStateResponseMapper;

    @Override
    public DeliveryStateResponseDto updateStatus(DeliveryStateRequestDto request) {
        return null;
    }
}
