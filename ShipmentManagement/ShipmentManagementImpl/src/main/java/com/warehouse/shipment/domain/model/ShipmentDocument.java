package com.warehouse.shipment.domain.model;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;

import java.time.Instant;
import java.util.UUID;

public record ShipmentDocument(UUID documentId, ShipmentId shipmentId, String filename,
                               String contentType, long sizeBytes, UserId uploadedByUserId,
                               Instant createdAt) {
}
