package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;

public interface RerouteTokenServicePort {
    void notifyShipmentStatusChanged(final ShipmentId shipmentId);
}
