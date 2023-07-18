package com.warehouse.parcelstate.infrastructure.adapter.primary;

import com.warehouse.parcelstate.infrastructure.adapter.primary.dto.ParcelDto;
import com.warehouse.parcelstate.infrastructure.adapter.primary.dto.RerouteParcelDto;
import com.warehouse.parcelstate.infrastructure.adapter.primary.dto.ShipmentParcelDto;

public interface ParcelStateService {

    ShipmentParcelDto shipParcel(ParcelDto parcel);
    RerouteParcelDto rerouteParcel(ParcelDto parcel);
}
