package com.warehouse.terminal.dto;

import java.time.Instant;

public record DeviceDto(DeviceIdDto deviceId, VersionDto version, DeviceTypeDto deviceType, UserIdDto userId,
                        DepartmentCodeDto depotCode, Instant lastUpdate, Boolean active) {
}
