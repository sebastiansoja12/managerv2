package com.warehouse.parcelstatuschange.domain.port.secondary;

import com.warehouse.parcelstatuschange.domain.vo.Parcel;

public interface ParcelStatusRepository {
    void update(Parcel parcel);
}
