package com.warehouse.reroute.domain.port.secondary;

import com.warehouse.reroute.domain.model.City;
import com.warehouse.reroute.domain.model.Parcel;
import com.warehouse.reroute.domain.vo.RerouteParcelResponse;

public interface ParcelRepository {

    RerouteParcelResponse updateParcel(Parcel parcel, City city);

}
