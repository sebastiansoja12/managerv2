package com.warehouse.reroute.domain.port.secondary;

import com.warehouse.reroute.domain.model.Parcel;
import com.warehouse.reroute.domain.model.RerouteParcel;
import com.warehouse.reroute.domain.vo.ParcelId;

public interface ParcelReroutePort {

    Parcel reroute(RerouteParcel request, ParcelId parcelId);

}
