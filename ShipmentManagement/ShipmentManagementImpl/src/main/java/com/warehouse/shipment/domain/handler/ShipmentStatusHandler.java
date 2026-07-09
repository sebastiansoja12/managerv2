package com.warehouse.shipment.domain.handler;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;

public interface ShipmentStatusHandler {
    boolean canHandle(final ShipmentStatus shipmentStatus);
    void notifyShipmentStatusChange(final ShipmentId shipmentId);
}
