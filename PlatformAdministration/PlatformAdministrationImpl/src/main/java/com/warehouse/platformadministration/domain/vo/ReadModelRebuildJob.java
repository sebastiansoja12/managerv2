package com.warehouse.platformadministration.domain.vo;

import com.warehouse.commonassets.identificator.OperatorId;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ReadModelRebuildJob(UUID jobId,
                                  String readModelType,
                                  String status,
                                  LocalDate dateFrom,
                                  LocalDate dateTo,
                                  OperatorId operatorId,
                                  Instant createdAt,
                                  Instant startedAt,
                                  Instant finishedAt,
                                  String error) {
}
