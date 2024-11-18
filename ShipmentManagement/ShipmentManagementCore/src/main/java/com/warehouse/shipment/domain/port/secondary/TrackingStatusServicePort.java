package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;

public interface TrackingStatusServicePort {
    void notifyShipmentStatusChanged(final ShipmentId shipmentId, final ShipmentStatus shipmentStatus);
}
