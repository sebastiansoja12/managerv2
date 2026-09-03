package com.warehouse.shipment.infrastructure.adapter.primary.api;

import java.time.Instant;
import java.util.UUID;

public record ShipmentDocumentResponseApi(UUID documentId, Long shipmentId, String filename,
                                          String contentType, long sizeBytes, Long uploadedByUserId,
                                          Instant createdAt) {
}
