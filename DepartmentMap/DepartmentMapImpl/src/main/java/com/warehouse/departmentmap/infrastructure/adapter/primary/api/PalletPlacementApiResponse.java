package com.warehouse.departmentmap.infrastructure.adapter.primary.api;

import java.time.Instant;

public record PalletPlacementApiResponse(String palletTrackingNumber, Long placedByUserId, Instant placedAt) {
}
