package com.warehouse.asyncjob.infrastructure.adapter.primary.dto;

import com.warehouse.asyncjob.domain.enumeration.AsyncJobStatus;
import com.warehouse.asyncjob.domain.enumeration.ReadModelType;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record AsyncJobResponse(UUID id,
                               AsyncJobStatus status,
                               ReadModelType readModelType,
                               LocalDate dateFrom,
                               LocalDate dateTo,
                               Long operatorId,
                               Instant createdAt,
                               Instant startedAt,
                               Instant finishedAt,
                               String error) {
}
