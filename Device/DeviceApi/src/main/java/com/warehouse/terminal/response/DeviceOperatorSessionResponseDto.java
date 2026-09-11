package com.warehouse.terminal.response;

import java.time.Instant;

import com.warehouse.terminal.dto.DeviceDto;

public record DeviceOperatorSessionResponseDto(DeviceDto device, Instant sessionExpiresAt) {
}
