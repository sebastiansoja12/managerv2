package com.warehouse.terminal.domain.vo;

import java.time.Instant;

public record DeviceHeartbeatResult(boolean updateRequired, Instant operatorSessionExpiresAt) {
}
