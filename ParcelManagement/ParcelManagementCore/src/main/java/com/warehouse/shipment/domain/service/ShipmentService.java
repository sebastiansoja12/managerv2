package com.warehouse.shipment.domain.service;

import com.warehouse.shipment.domain.model.*;

public interface ShipmentService {

    ShipmentResponse ship(ShipmentRequest request);

    Parcel loadParcel(Long parcelId);

    UpdateParcelResponse update(ParcelUpdate parcelUpdate);

    void delete(Long parcelId);
}
