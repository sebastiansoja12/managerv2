package com.warehouse.shipment.application.port.primary.result;

import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public record ShipmentRerouteResult(ShipmentSnapshot snapshot, boolean reroutable) {
}
