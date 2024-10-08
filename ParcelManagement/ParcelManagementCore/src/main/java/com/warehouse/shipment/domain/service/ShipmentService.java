package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.vo.Parcel;
import com.warehouse.shipment.domain.vo.ShipmentResponse;
import com.warehouse.shipment.domain.vo.ShipmentUpdateResponse;

public interface ShipmentService {

    ShipmentResponse createShipment(final Shipment parcel);

    Parcel loadParcel(final ShipmentId shipmentId);

    ShipmentUpdateResponse update(final ShipmentUpdate shipmentUpdate);

    boolean exists(final ShipmentId shipmentId);
}
