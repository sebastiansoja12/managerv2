package com.warehouse.shipment.application.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public interface ShipmentReadModelSyncService {

    void sync(final ShipmentSnapshot snapshot);

    void syncReadModel(final ShipmentId shipmentId);

    int syncReadModels();
}
