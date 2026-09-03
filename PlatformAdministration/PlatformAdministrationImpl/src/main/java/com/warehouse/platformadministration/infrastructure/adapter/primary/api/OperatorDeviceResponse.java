package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

import java.time.Instant;

public record OperatorDeviceResponse(String deviceId,
                                     String deviceType,
                                     String status,
                                     Long userId,
                                     String departmentCode,
                                     String version,
                                     Instant lastUpdate,
                                     boolean paired,
                                     Instant pairKeyExpiresAt,
                                     Instant lastLoginAt) {
}
