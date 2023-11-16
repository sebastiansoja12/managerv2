package com.warehouse.deliverytoken.domain.port.secondary;

import com.warehouse.deliverytoken.domain.vo.Parcel;
import com.warehouse.deliverytoken.domain.vo.ParcelId;

public interface ParcelServicePort {
    Parcel downloadParcel(ParcelId parcelId);
}
