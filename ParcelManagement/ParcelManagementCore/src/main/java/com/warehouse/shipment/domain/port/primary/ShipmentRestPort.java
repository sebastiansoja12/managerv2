package com.warehouse.shipment.domain.port.primary;

import com.warehouse.shipment.domain.model.Parcel;

public interface ShipmentRestPort {

    void delete(Long parcelId);

    Parcel loadParcel(Long parcelId);
}
