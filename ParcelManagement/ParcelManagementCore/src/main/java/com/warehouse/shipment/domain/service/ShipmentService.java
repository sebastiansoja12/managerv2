package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.vo.Parcel;
import com.warehouse.shipment.domain.vo.ShipmentResponse;
import com.warehouse.shipment.domain.vo.ShipmentUpdateResponse;

public interface ShipmentService {

    ShipmentResponse createShipment(Shipment parcel);

    Parcel loadParcel(final ShipmentId shipmentId);

    ShipmentUpdateResponse update(ShipmentUpdate shipmentUpdate);

    boolean exists(Long parcelId);
}
