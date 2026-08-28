package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.application.port.secondary.ShipmentIdGenerator;

public class ShipmentIdGeneratorAdapter implements ShipmentIdGenerator {

    @Override
    public ShipmentId nextId() {
        return ShipmentId.nextId();
    }
}
