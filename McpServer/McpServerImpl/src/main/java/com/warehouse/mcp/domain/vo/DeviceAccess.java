package com.warehouse.mcp.domain.vo;

import java.time.Instant;

public record DeviceAccess(String deviceId,
                           String deviceType,
                           String status,
                           String departmentCode,
                           String assignmentMode,
                           Long ownerUserId,
                           String version,
                           boolean paired,
                           Instant lastSeenAt,
                           Long operatorUserId,
                           Instant operatorSessionExpiresAt) {
}
