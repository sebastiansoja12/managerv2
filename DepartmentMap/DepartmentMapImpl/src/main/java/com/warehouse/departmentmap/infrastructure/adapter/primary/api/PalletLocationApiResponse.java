package com.warehouse.departmentmap.infrastructure.adapter.primary.api;

import java.time.Instant;
import java.util.UUID;

public record PalletLocationApiResponse(String palletTrackingNumber,
                                        UUID mapId,
                                        String mapName,
                                        Long departmentId,
                                        UUID locationId,
                                        String locationCode,
                                        Long placedByUserId,
                                        Instant placedAt) {
}
