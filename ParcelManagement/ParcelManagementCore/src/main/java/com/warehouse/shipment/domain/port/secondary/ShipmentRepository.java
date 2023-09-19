package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.domain.model.ParcelUpdate;
import com.warehouse.shipment.domain.model.ShipmentParcel;

public interface ShipmentRepository {

    Parcel save(ShipmentParcel parcel);

    Parcel update(ParcelUpdate parcel);

    Parcel loadParcelById(Long parcelId);

    boolean exists(Long parcelId);
}
