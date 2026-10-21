package com.warehouse.departmentmap.infrastructure.adapter.primary.api;

import java.time.Instant;
import java.util.UUID;

public record PalletMovementApiResponse(UUID movementId,
                                        String palletTrackingNumber,
                                        String movementType,
                                        UUID mapId,
                                        UUID fromLocationId,
                                        String fromLocationCode,
                                        UUID toLocationId,
                                        String toLocationCode,
                                        Long userId,
                                        Instant occurredAt) {
}
