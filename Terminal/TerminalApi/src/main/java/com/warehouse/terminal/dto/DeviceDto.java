package com.warehouse.terminal.dto;

import java.time.Instant;

public record DeviceDto(DeviceIdDto deviceId, VersionDto version, DeviceTypeDto deviceType, UserIdDto userId,
                        DepotCodeDto depotCode, Instant lastUpdate, Boolean active) {
}