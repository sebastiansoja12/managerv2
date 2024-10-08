package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.Parcel;
import com.warehouse.shipment.domain.model.ShipmentUpdate;

public interface ShipmentRepository {

    Parcel save(final Shipment parcel);

    Parcel update(final ShipmentUpdate parcel);

    Parcel findParcelById(final ShipmentId shipmentId);

    boolean exists(final ShipmentId shipmentId);
}
