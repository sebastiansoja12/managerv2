package com.warehouse.asyncjob.infrastructure.adapter.secondary.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.warehouse.asyncjob.domain.model.AsyncJob;
import com.warehouse.asyncjob.domain.model.AsyncJobStatus;
import com.warehouse.asyncjob.domain.model.ReadModelType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "async_job")
@Entity(name = "asyncJob.AsyncJobEntity")
public class AsyncJobEntity {

    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "id", nullable = false, length = 36)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AsyncJobStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "read_model_type", nullable = false)
    private ReadModelType readModelType;

    @Column(name = "date_from", nullable = false)
    private LocalDate dateFrom;

    @Column(name = "date_to", nullable = false)
    private LocalDate dateTo;

    @Column(name = "operator_id", nullable = false)
    private Long operatorId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    @Column(name = "error")
    private String error;

    protected AsyncJobEntity() {
    }

    private AsyncJobEntity(final UUID id,
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

    public static AsyncJobEntity from(final AsyncJob job) {
        return new AsyncJobEntity(job.getId(), job.getStatus(), job.getReadModelType(), job.getDateFrom(),
                job.getDateTo(), job.getOperatorId(), job.getCreatedAt(), job.getStartedAt(), job.getFinishedAt(),
                job.getError());
    }

    public AsyncJob toDomain() {
        return new AsyncJob(id, status, readModelType, dateFrom, dateTo, operatorId, createdAt, startedAt,
                finishedAt, error);
    }
}
