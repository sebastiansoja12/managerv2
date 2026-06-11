package com.warehouse.tracking.infrastructure.adapter.primary.api;

import java.util.Map;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TrackingIntegrationApiRequest(boolean enabled,
                                            @NotNull @Size(max = 32) Map<String, String> values) {
}
