package com.warehouse.shipment.application.port.secondary;

import com.warehouse.shipment.api.event.snapshot.ShipmentEventData;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public interface ShipmentEventDataMapperPort {

    ShipmentEventData map(final ShipmentSnapshot shipment);
}
