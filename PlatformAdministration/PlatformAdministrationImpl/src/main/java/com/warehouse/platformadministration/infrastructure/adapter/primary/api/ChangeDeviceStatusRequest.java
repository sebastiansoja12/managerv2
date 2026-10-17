package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.NotBlank;

public record ChangeDeviceStatusRequest(@NotBlank String status) {
}
