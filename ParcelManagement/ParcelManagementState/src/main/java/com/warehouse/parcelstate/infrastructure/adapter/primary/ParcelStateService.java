package com.warehouse.parcelstate.infrastructure.adapter.primary;

import com.warehouse.parcelstate.infrastructure.adapter.primary.dto.RerouteRequestDto;
import com.warehouse.parcelstate.infrastructure.adapter.primary.dto.RerouteResponseDto;
import com.warehouse.parcelstate.infrastructure.adapter.primary.dto.ShipmentParcelDto;

public interface ParcelStateService {

    ShipmentParcelDto shipParcel(RerouteRequestDto parcel);
    RerouteResponseDto rerouteParcel(RerouteRequestDto parcel);
}
