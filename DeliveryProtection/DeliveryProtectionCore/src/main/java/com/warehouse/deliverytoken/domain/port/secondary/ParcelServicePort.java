package com.warehouse.deliverytoken.domain.port.secondary;

import com.warehouse.deliverytoken.domain.model.Parcel;
import com.warehouse.deliverytoken.domain.model.ParcelId;

public interface ParcelServicePort {
    Parcel downloadParcel(ParcelId parcelId);
}
