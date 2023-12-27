package com.warehouse.shipment.domain.service;

import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.model.ParcelUpdate;
import com.warehouse.shipment.domain.vo.ShipmentResponse;
import com.warehouse.shipment.domain.vo.UpdateParcelResponse;

public interface ShipmentService {

    ShipmentResponse createShipment(ShipmentParcel parcel);

    Parcel loadParcel(Long parcelId);

    UpdateParcelResponse update(ParcelUpdate parcelUpdate);

    boolean exists(Long parcelId);
}
