package com.warehouse.asyncjob.infrastructure.adapter.primary.dto;

import com.warehouse.asyncjob.domain.model.AsyncJob;
import com.warehouse.asyncjob.domain.model.AsyncJobStatus;
import com.warehouse.asyncjob.domain.model.ReadModelType;

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

    public static AsyncJobResponse from(final AsyncJob job) {
        return new AsyncJobResponse(job.getId(), job.getStatus(), job.getReadModelType(), job.getDateFrom(),
                job.getDateTo(), job.getOperatorId(), job.getCreatedAt(), job.getStartedAt(),
                job.getFinishedAt(), job.getError());
    }
}
