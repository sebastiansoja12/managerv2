package com.warehouse.asyncjob.infrastructure.adapter.primary.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RebuildReadModelRequest(@NotNull Long operatorId,
                                      @NotNull LocalDate dateFrom,
                                      @NotNull LocalDate dateTo) {
}
