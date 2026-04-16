package com.warehouse.deliverymissed.domain.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;

public interface DeliveryMissedDetailsRepository {
    Integer count(final ShipmentId shipmentId);
}
