package com.warehouse.shipment.application.port.primary.result;

import com.warehouse.shipment.domain.vo.ShipmentDeliveryDetails;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public record ShipmentResult(ShipmentSnapshot snapshot, ShipmentDeliveryDetails deliveryDetails) {

    public ShipmentResult(final ShipmentSnapshot snapshot) {
        this(snapshot, null);
    }
}
