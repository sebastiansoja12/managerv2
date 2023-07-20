package com.warehouse.parcelstate.infrastructure.adapter.primary;

import com.warehouse.parcelstate.domain.model.RerouteParcel;
import com.warehouse.parcelstate.domain.model.RerouteResponse;
import com.warehouse.parcelstate.domain.port.primary.ParcelStatePort;
import com.warehouse.parcelstate.infrastructure.adapter.primary.dto.RerouteRequestDto;

import com.warehouse.parcelstate.infrastructure.adapter.primary.dto.RerouteResponseDto;
import com.warehouse.parcelstate.infrastructure.adapter.primary.dto.ShipmentParcelDto;
import com.warehouse.parcelstate.infrastructure.adapter.primary.mapper.ParcelStateRequestMapper;
import com.warehouse.parcelstate.infrastructure.adapter.primary.mapper.ParcelStateResponseMapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ParcelStateServiceAdapter implements ParcelStateService {

    private final ParcelStatePort parcelStatePort;

    private final ParcelStateRequestMapper parcelStateRequestMapper;

    private final ParcelStateResponseMapper parcelStateResponseMapper;

    @Override
    public ShipmentParcelDto shipParcel(RerouteRequestDto parcel) {
        return null;
    }

    @Override
    public RerouteResponseDto rerouteParcel(RerouteRequestDto rerouteRequest) {
        final RerouteParcel parcel = parcelStateRequestMapper.map(rerouteRequest);
        final RerouteResponse rerouteResponse = parcelStatePort.rerouteParcel(parcel);
        return parcelStateResponseMapper.map(rerouteResponse);
    }
}
