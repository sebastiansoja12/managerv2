package com.warehouse.departmentmap.api.dto;

import java.time.Instant;
import java.util.UUID;

public record PalletLocationDto(String palletTrackingNumber,
                                UUID mapId,
                                String mapName,
                                Long departmentId,
                                UUID locationId,
                                String locationCode,
                                Instant placedAt) {
}
