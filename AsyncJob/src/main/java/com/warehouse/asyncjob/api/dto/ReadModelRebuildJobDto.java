package com.warehouse.asyncjob.api.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ReadModelRebuildJobDto(UUID jobId,
                                     String status,
                                     String readModelType,
                                     LocalDate dateFrom,
                                     LocalDate dateTo,
                                     Long operatorId,
                                     Instant createdAt,
                                     Instant startedAt,
                                     Instant finishedAt,
                                     String error) {
}
