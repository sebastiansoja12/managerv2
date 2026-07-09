package com.warehouse.terminal.dto;

import java.time.Instant;

public record DeviceDto(DeviceIdDto deviceId, VersionDto version, DeviceTypeDto deviceType, UsernameDto username,
                        DepartmentCodeDto depotCode, Instant lastUpdate, Boolean active) {
}
