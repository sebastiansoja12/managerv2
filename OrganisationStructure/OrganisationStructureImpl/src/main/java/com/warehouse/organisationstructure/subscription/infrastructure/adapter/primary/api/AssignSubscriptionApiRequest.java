package com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AssignSubscriptionApiRequest(@NotBlank String plan,
                                           @NotNull LocalDate startDate,
                                           @NotNull LocalDate endDate) {
}
