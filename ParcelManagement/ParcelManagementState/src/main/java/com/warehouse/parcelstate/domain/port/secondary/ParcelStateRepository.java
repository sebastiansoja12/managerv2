package com.warehouse.parcelstate.domain.port.secondary;


import com.warehouse.parcelstate.domain.model.Parcel;

public interface ParcelStateRepository {

    Long save(Parcel parcel);

    void delete(Long parcelId);

    Parcel loadParcelById(Long parcelId);

    Parcel update(Parcel parcelUpdate);
}
