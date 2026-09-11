package com.warehouse.terminal.domain.vo;

import java.time.Instant;

public record IssuedDevicePairKey(DevicePairKey pairKey, Instant expiresAt) {
}
