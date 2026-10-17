package com.warehouse.reroute.infrastructure.adapter.primary.api;

import java.time.Instant;

public record RerouteAcceptedApiResponse(String trackingNumber, Instant requestedAt) {
}
