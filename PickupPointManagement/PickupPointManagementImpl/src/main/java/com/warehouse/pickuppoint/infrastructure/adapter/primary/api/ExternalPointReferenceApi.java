package com.warehouse.pickuppoint.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.NotBlank;

public record ExternalPointReferenceApi(@NotBlank String networkCode, @NotBlank String pointCode) {
}
