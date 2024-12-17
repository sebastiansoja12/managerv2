package com.warehouse.deliveryreturn.domain.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliveryreturn.domain.vo.Shipment;

public interface ShipmentRepositoryServicePort {
    Shipment downloadShipment(final ShipmentId shipmentId);
}
