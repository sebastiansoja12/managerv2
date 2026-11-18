package com.warehouse.terminal.response;

import java.time.Instant;

public record DeviceHeartbeatResponseDto(Boolean updateRequired, Instant operatorSessionExpiresAt) {
}
