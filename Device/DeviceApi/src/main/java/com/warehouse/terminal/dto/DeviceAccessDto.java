package com.warehouse.terminal.dto;

import java.time.Instant;

public record DeviceAccessDto(String deviceId,
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
