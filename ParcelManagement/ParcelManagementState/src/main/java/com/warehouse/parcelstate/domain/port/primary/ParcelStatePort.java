package com.warehouse.parcelstate.domain.port.primary;

import com.warehouse.parcelstate.domain.model.RerouteParcel;
import com.warehouse.parcelstate.domain.model.RerouteResponse;

public interface ParcelStatePort {

    RerouteResponse rerouteParcel(RerouteParcel parcel);
}
