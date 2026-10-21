package com.warehouse.reroute.application.port.primary.result;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.TrackingNumber;

public record ReroutableShipmentSummary(TrackingNumber trackingNumber,
                                        ShipmentStatus status,
                                        boolean reroutable,
                                        String maskedEmail) {
}
