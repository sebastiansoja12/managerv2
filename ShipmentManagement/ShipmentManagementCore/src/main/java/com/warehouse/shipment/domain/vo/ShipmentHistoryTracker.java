package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;

import java.time.Instant;

public record ShipmentHistoryTracker(ShipmentId shipmentId, ShipmentStatus previousStatus,
                                     String errorMessage, String errorCause, boolean successful,
                                     Instant timestamp, UserId userId) {
}
