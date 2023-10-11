package com.warehouse.shipment.domain.service;

import com.warehouse.shipment.domain.model.*;

public interface ShipmentService {

    ShipmentResponse createShipment(ShipmentParcel parcel);

    Parcel loadParcel(Long parcelId);

    UpdateParcelResponse update(ParcelUpdate parcelUpdate);

    boolean exists(Long parcelId);
}
