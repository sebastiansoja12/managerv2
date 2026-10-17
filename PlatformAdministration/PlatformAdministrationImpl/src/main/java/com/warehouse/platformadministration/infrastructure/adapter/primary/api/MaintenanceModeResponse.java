package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

import java.time.Instant;

public record MaintenanceModeResponse(boolean enabled, String message, String updatedBy, Instant updatedAt) {
}
