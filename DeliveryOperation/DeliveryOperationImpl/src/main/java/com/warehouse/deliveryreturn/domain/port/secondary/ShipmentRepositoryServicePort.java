package com.warehouse.deliveryreturn.domain.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliveryreturn.domain.vo.DeliverableShipment;

public interface ShipmentRepositoryServicePort {
    DeliverableShipment downloadShipment(final ShipmentId shipmentId);
}
