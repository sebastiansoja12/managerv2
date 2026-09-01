package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.NotBlank;

public record ChangePlatformOwnerPasswordRequest(@NotBlank String password) {
}
