package com.warehouse.reroute.infrastructure.adapter.primary.api;

import java.time.Instant;

public record RerouteTokenIssuedApiResponse(String maskedEmail, Instant expiresAt) {
}
