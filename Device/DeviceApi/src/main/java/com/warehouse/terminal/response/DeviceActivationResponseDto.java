package com.warehouse.terminal.response;

import java.time.Instant;

import com.warehouse.terminal.dto.DeviceDto;

public record DeviceActivationResponseDto(DeviceDto device, String pairKey, Instant pairKeyExpiresAt) {
}
