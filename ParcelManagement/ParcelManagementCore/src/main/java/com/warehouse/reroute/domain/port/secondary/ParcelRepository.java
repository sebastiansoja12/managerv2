package com.warehouse.reroute.domain.port.secondary;

import com.warehouse.reroute.domain.model.City;
import com.warehouse.reroute.domain.model.Parcel;
import com.warehouse.reroute.domain.vo.ParcelUpdateResponse;

public interface ParcelRepository {

    ParcelUpdateResponse updateParcel(Parcel parcel, City city);

}
