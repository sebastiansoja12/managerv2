package com.warehouse.logistics.application.port.primary.result;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public record ShipmentAcceptDetail(TrackingNumber trackingNumber,
                                   ShipmentId shipmentId,
                                   boolean accepted,
                                   String errorMessage) {

    public static ShipmentAcceptDetail accepted(final TrackingNumber trackingNumber, final ShipmentId shipmentId) {
        return new ShipmentAcceptDetail(trackingNumber, shipmentId, true, null);
    }

    public static ShipmentAcceptDetail failed(final TrackingNumber trackingNumber, final String errorMessage) {
        return new ShipmentAcceptDetail(trackingNumber, null, false, errorMessage);
    }
}
