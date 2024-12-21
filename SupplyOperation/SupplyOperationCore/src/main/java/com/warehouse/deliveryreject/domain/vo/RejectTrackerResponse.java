package com.warehouse.deliveryreject.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;

import java.util.UUID;

public record RejectTrackerResponse(ShipmentId shipmentId, ShipmentId newShipmentId, UUID processId) {
}
