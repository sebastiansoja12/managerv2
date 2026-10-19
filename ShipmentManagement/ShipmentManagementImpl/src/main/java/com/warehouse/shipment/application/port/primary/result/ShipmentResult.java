package com.warehouse.shipment.application.port.primary.result;

import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.domain.model.Signature;

public record ShipmentResult(ShipmentSnapshot snapshot,
                             Signature signature) {

    public ShipmentResult(final ShipmentSnapshot snapshot) {
        this(snapshot, null);
    }
}
