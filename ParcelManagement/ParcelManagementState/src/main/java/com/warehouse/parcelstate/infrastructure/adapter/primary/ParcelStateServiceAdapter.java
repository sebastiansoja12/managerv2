package com.warehouse.parcelstate.infrastructure.adapter.primary;

import com.warehouse.parcelstate.domain.model.Parcel;
import com.warehouse.parcelstate.domain.model.RerouteParcel;
import com.warehouse.parcelstate.domain.port.primary.ParcelStatePort;
import com.warehouse.parcelstate.infrastructure.adapter.primary.dto.ParcelDto;

import com.warehouse.parcelstate.infrastructure.adapter.primary.dto.RerouteParcelDto;
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
    public ShipmentParcelDto shipParcel(ParcelDto parcel) {
        return null;
    }

    @Override
    public RerouteParcelDto rerouteParcel(ParcelDto parcelRequest) {
        final Parcel parcel = parcelStateRequestMapper.map(parcelRequest);
        final RerouteParcel rerouteParcel = parcelStatePort.rerouteParcel(parcel);
        return parcelStateResponseMapper.map(rerouteParcel);
    }
}
