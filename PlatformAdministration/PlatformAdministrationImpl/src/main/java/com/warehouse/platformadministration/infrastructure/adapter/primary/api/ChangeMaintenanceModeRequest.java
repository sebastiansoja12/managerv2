package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChangeMaintenanceModeRequest(@NotNull Boolean enabled, @Size(max = 500) String message) {
}
