package com.warehouse.fleettracking.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DeviceIdDto(@NotBlank @Size(max = 100) String value) {
}
