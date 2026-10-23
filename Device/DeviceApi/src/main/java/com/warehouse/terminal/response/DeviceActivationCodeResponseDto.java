package com.warehouse.terminal.response;

import java.time.Instant;

public record DeviceActivationCodeResponseDto(String activationCode, Instant expiresAt) {
}
