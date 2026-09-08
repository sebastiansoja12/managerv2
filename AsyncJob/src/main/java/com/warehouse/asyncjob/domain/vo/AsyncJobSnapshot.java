package com.warehouse.asyncjob.domain.vo;

import com.warehouse.asyncjob.domain.enumeration.AsyncJobStatus;
import com.warehouse.asyncjob.domain.enumeration.ReadModelType;
import com.warehouse.commonassets.identificator.OperatorId;

import java.time.Instant;
import java.time.LocalDate;

public record AsyncJobSnapshot(AsyncJobId id,
                               AsyncJobStatus status,
                               ReadModelType readModelType,
                               LocalDate dateFrom,
                               LocalDate dateTo,
                               OperatorId operatorId,
                               Instant createdAt,
                               Instant startedAt,
                               Instant finishedAt,
                               String error) {
}
