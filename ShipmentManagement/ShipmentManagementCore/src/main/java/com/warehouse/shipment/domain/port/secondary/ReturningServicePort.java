package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public interface ReturningServicePort {
    void notifyShipmentReturn(final ShipmentSnapshot snapshot);

    void notifyShipmentUpdated(final ShipmentSnapshot snapshot);
}
