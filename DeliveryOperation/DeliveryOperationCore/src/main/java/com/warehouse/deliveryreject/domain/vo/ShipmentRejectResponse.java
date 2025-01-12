package com.warehouse.deliveryreject.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;

public record ShipmentRejectResponse(ShipmentId shipmentId, ShipmentId newShipmentId, Boolean loggedInTracker) {
}
