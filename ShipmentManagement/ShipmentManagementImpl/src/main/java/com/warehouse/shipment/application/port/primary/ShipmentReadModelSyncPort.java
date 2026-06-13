package com.warehouse.shipment.application.port.primary;

import com.warehouse.commonassets.identificator.ShipmentId;

public interface ShipmentReadModelSyncPort {

    void syncReadModel(final ShipmentId shipmentId);

    int syncReadModels();
}
