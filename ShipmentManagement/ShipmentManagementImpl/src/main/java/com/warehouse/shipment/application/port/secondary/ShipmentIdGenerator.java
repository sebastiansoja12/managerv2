package com.warehouse.shipment.application.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;

public interface ShipmentIdGenerator {

    ShipmentId nextId();
}
