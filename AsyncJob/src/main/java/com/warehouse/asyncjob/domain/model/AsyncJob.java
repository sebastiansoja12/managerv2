package com.warehouse.asyncjob.domain.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class AsyncJob {

    private final UUID id;
    private AsyncJobStatus status;
    private final ReadModelType readModelType;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;
    private final Long operatorId;
    private final Instant createdAt;
    private Instant startedAt;
    private Instant finishedAt;
    private String error;

    public AsyncJob(final UUID id,
                    final AsyncJobStatus status,
                    final ReadModelType readModelType,
                    final LocalDate dateFrom,
                    final LocalDate dateTo,
                    final Long operatorId,
                    final Instant createdAt,
                    final Instant startedAt,
                    final Instant finishedAt,
                    final String error) {
        this.id = id;
        this.status = status;
        this.readModelType = readModelType;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.operatorId = operatorId;
        this.createdAt = createdAt;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.error = error;
    }

    public static AsyncJob pending(final ReadModelType readModelType,
                                   final Long operatorId,
                                   final LocalDate dateFrom,
                                   final LocalDate dateTo) {
        return new AsyncJob(UUID.randomUUID(), AsyncJobStatus.PENDING, readModelType, dateFrom, dateTo, operatorId,
                Instant.now(), null, null, null);
    }

    public void markRunning() {
        this.status = AsyncJobStatus.RUNNING;
        this.startedAt = Instant.now();
        this.error = null;
    }

    public void markCompleted() {
        this.status = AsyncJobStatus.COMPLETED;
        this.finishedAt = Instant.now();
    }

    public void markFailed(final Throwable exception) {
        this.status = AsyncJobStatus.FAILED;
        this.finishedAt = Instant.now();
        this.error = exception.getMessage();
    }

    public UUID getId() {
        return id;
    }

    public AsyncJobStatus getStatus() {
        return status;
    }

    public ReadModelType getReadModelType() {
        return readModelType;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public String getError() {
        return error;
    }
}
