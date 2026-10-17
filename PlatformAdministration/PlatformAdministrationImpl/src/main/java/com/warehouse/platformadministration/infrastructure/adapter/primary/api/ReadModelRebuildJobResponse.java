package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ReadModelRebuildJobResponse(UUID jobId,
                                          String readModelType,
                                          String status,
                                          LocalDate dateFrom,
                                          LocalDate dateTo,
                                          Long operatorId,
                                          Instant createdAt,
                                          Instant startedAt,
                                          Instant finishedAt,
                                          String error) {
}
