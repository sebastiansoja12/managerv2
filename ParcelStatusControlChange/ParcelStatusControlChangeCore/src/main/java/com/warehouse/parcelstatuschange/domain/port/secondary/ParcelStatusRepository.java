package com.warehouse.parcelstatuschange.domain.port.secondary;

import com.warehouse.parcelstatuschange.domain.vo.Parcel;
import com.warehouse.parcelstatuschange.domain.vo.Status;

public interface ParcelStatusRepository {
    void update(Parcel parcel);

    Status get(Long parcelId);
}
