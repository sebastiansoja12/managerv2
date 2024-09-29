package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.identificator.ParcelId;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.vo.Parcel;
import com.warehouse.shipment.domain.vo.ShipmentResponse;
import com.warehouse.shipment.domain.vo.ShipmentUpdateResponse;

public interface ShipmentService {

    ShipmentResponse createShipment(ShipmentParcel parcel);

    Parcel loadParcel(final ParcelId parcelId);

    ShipmentUpdateResponse update(ShipmentUpdate shipmentUpdate);

    boolean exists(Long parcelId);
}
