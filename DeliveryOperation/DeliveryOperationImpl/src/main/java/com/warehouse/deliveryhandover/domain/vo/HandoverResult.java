package com.warehouse.deliveryhandover.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public record HandoverResult(TrackingNumber trackingNumber,
                             ShipmentId shipmentId,
                             boolean handedOver,
                             String errorMessage) {

    public static HandoverResult handedOver(final TrackingNumber trackingNumber, final ShipmentId shipmentId) {
        return new HandoverResult(trackingNumber, shipmentId, true, null);
    }

    public static HandoverResult failed(final TrackingNumber trackingNumber, final String errorMessage) {
        return new HandoverResult(trackingNumber, null, false, errorMessage);
    }
}
