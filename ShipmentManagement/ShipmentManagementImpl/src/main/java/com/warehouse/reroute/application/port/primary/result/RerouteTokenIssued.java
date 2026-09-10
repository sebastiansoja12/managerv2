package com.warehouse.reroute.application.port.primary.result;

import java.time.Instant;

public record RerouteTokenIssued(String maskedEmail, Instant expiresAt) {
}
