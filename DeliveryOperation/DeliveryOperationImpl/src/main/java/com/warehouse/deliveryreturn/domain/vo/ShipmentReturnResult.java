package com.warehouse.deliveryreturn.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public record ShipmentReturnResult(UpdateStatus updateStatus,
                                   ShipmentId newShipmentId,
                                   TrackingNumber newTrackingNumber,
                                   String errorMessage) {

    public static ShipmentReturnResult returned(final ShipmentId newShipmentId, final TrackingNumber newTrackingNumber) {
        return new ShipmentReturnResult(UpdateStatus.OK, newShipmentId, newTrackingNumber, null);
    }

    public static ShipmentReturnResult failed(final String errorMessage) {
        return new ShipmentReturnResult(UpdateStatus.NOT_OK, null, null, errorMessage);
    }
}
