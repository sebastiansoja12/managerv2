package com.warehouse.onboarding.infrastructure.adapter.primary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record AcceptConsentsApiRequest(@NotNull Set<String> acceptedConsents, @NotBlank String version) {
}
