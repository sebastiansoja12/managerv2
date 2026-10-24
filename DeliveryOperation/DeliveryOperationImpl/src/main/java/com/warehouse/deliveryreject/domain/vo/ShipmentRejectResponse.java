package com.warehouse.deliveryreject.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public record ShipmentRejectResponse(
        ShipmentId shipmentId,
        ShipmentId newShipmentId,
        Boolean loggedInTracker,
        Boolean success,
        String errorMessage,
        TrackingNumber newTrackingNumber) {
}
