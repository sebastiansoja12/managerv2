package com.warehouse.suppliertoken.domain.port.secondary;

import com.warehouse.suppliertoken.domain.model.Parcel;
import com.warehouse.suppliertoken.domain.model.ParcelId;

import java.util.List;

public interface ParcelServicePort {
    List<Parcel> downloadParcels(List<ParcelId> parcelIds);
}
