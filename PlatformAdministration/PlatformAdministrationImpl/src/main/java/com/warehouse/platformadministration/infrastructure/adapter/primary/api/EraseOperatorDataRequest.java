package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.NotBlank;

public record EraseOperatorDataRequest(@NotBlank String confirmation) {
}
