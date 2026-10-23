package com.warehouse.terminal.domain.vo;

import java.time.Instant;

public record DeviceActivationCodeIssue(ActivationCode activationCode, Instant expiresAt) {
}
