package com.warehouse.shipment.application.port.primary;

import com.warehouse.shipment.domain.vo.ShipmentDeliveryDetails;
import com.warehouse.commonassets.identificator.ShipmentId;

import java.time.LocalDate;

public interface ShipmentReadModelSyncPort {

    void syncReadModel(final ShipmentId shipmentId);

    int syncReadModels(final LocalDate dateFrom, final LocalDate dateTo);

    void syncReadModel(final ShipmentId shipmentId, final ShipmentDeliveryDetails deliveryDetails);
}
