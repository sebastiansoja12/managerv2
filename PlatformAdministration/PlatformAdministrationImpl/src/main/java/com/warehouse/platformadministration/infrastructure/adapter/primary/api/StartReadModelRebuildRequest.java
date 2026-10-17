package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record StartReadModelRebuildRequest(@NotNull LocalDate dateFrom, @NotNull LocalDate dateTo) {
}
