package com.warehouse.shipment.application.port.primary;

import java.time.LocalDate;

import com.warehouse.commonassets.identificator.ShipmentId;

public interface ShipmentReadModelSyncPort {

    void syncReadModel(final ShipmentId shipmentId);

    int syncReadModels();

    int syncReadModels(final LocalDate dateFrom, final LocalDate dateTo);
}
