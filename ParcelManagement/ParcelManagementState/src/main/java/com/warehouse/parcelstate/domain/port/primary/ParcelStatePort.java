package com.warehouse.parcelstate.domain.port.primary;

import com.warehouse.parcelstate.domain.model.Parcel;
import com.warehouse.parcelstate.domain.model.RerouteParcel;

public interface ParcelStatePort {

    RerouteParcel rerouteParcel(Parcel parcel);
}
