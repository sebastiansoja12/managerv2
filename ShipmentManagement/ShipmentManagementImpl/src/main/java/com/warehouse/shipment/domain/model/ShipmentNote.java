package com.warehouse.shipment.domain.model;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;

import java.time.Instant;
import java.util.UUID;

public record ShipmentNote(UUID noteId, ShipmentId shipmentId, String body, boolean internal,
                           UserId authorUserId, Instant createdAt) {

    public ShipmentNote {
        if (body == null || body.isBlank() || body.length() > 4000) {
            throw new IllegalArgumentException("Note must contain 1 to 4000 characters");
        }
    }
}
