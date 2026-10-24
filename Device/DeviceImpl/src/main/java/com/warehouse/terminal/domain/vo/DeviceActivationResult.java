package com.warehouse.terminal.domain.vo;

import java.time.Instant;

import com.warehouse.terminal.domain.model.Device;

public record DeviceActivationResult(Device device, DevicePairKey pairKey, Instant pairKeyExpiresAt) {
}
