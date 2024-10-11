package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.ShipmentUpdate;

public interface ShipmentService {

    void createShipment(final Shipment shipment);

    Shipment loadShipment(final ShipmentId shipmentId);

    void updateShipment(final ShipmentUpdate shipmentUpdate, final ShipmentId shipmentId);

    boolean existsShipment(final ShipmentId shipmentId);

    ShipmentId nextShipmentId();
}
