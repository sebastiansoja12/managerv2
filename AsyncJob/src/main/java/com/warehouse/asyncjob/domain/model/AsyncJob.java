package com.warehouse.asyncjob.domain.model;

import com.warehouse.asyncjob.domain.enumeration.AsyncJobStatus;
import com.warehouse.asyncjob.domain.enumeration.ReadModelType;
import com.warehouse.asyncjob.domain.vo.AsyncJobId;
import com.warehouse.asyncjob.domain.vo.AsyncJobSnapshot;
import com.warehouse.commonassets.identificator.OperatorId;

import java.time.Instant;
import java.time.LocalDate;

public class AsyncJob {

    private final AsyncJobId id;
    private AsyncJobStatus status;
    private final ReadModelType readModelType;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;
    private final OperatorId operatorId;
    private final Instant createdAt;
    private Instant startedAt;
    private Instant finishedAt;
    private String error;

    public AsyncJob(final AsyncJobId id,
                    final AsyncJobStatus status,
                    final ReadModelType readModelType,
                    final LocalDate dateFrom,
                    final LocalDate dateTo,
                    final OperatorId operatorId,
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
                                   final OperatorId operatorId,
                                   final LocalDate dateFrom,
                                   final LocalDate dateTo) {
        return new AsyncJob(AsyncJobId.generate(), AsyncJobStatus.PENDING, readModelType, dateFrom, dateTo, operatorId,
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

    public void markFailed(final String failureReason) {
        this.status = AsyncJobStatus.FAILED;
        this.finishedAt = Instant.now();
        this.error = failureReason;
    }

    public AsyncJobSnapshot snapshot() {
        return new AsyncJobSnapshot(id, status, readModelType, dateFrom, dateTo, operatorId, createdAt, startedAt,
                finishedAt, error);
    }

    public AsyncJobId getId() {
        return id;
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

    public OperatorId getOperatorId() {
        return operatorId;
    }
}
