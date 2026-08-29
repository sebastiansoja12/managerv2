package com.warehouse.shipment.infrastructure.adapter.primary.api;

import java.time.Instant;
import java.util.UUID;

public record ShipmentNoteResponseApi(UUID noteId, Long shipmentId, String body, boolean internal,
                                      Long authorUserId, Instant createdAt) {
}
