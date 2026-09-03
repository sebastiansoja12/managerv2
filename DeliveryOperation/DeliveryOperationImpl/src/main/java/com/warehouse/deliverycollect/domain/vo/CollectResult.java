package com.warehouse.deliverycollect.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public record CollectResult(TrackingNumber trackingNumber,
                            ShipmentId shipmentId,
                            boolean collected,
                            String errorMessage) {

    public static CollectResult collected(final TrackingNumber trackingNumber, final ShipmentId shipmentId) {
        return new CollectResult(trackingNumber, shipmentId, true, null);
    }

    public static CollectResult failed(final TrackingNumber trackingNumber, final String errorMessage) {
        return new CollectResult(trackingNumber, null, false, errorMessage);
    }
}
