package com.warehouse.reroute.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.NotBlank;

public record RerouteTokenVerificationApiRequest(@NotBlank String token) {
}
