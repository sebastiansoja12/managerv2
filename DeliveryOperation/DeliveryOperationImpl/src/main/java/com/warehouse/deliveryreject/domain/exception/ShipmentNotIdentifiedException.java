package com.warehouse.deliveryreject.domain.exception;

import com.warehouse.commonassets.identificator.TrackingNumber;

public class ShipmentNotIdentifiedException extends RuntimeException {

    public ShipmentNotIdentifiedException(final TrackingNumber trackingNumber) {
        super("Shipment with tracking number %s not found".formatted(trackingNumber == null ? null : trackingNumber.value()));
    }
}
