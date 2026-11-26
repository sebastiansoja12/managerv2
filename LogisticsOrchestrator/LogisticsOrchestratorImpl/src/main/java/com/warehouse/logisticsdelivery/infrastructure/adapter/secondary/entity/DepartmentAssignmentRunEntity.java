package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.DepartmentAssignmentRunId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.logisticsdelivery.domain.enumeration.CourierAssignmentDateField;
import com.warehouse.logisticsdelivery.domain.enumeration.DepartmentAssignmentRunStatus;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Entity(name = "delivery.DepartmentAssignmentRunEntity")
@Table(name = "department_assignment_run")
public class DepartmentAssignmentRunEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false))
    private DepartmentAssignmentRunId runId;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "department_id", nullable = false))
    private DepartmentId departmentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "date_field", nullable = false)
    private CourierAssignmentDateField dateField;

    @Column(name = "window_from", nullable = false)
    private Instant windowFrom;

    @Column(name = "window_to", nullable = false)
    private Instant windowTo;

    @Column(name = "backlog_from", nullable = false)
    private Instant backlogFrom;

    @Column(name = "batch_size", nullable = false)
    private int batchSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DepartmentAssignmentRunStatus status;

    @Column(name = "cursor_date")
    private LocalDateTime cursorDate;

    @Column(name = "cursor_id")
    private String cursorId;

    @Column(name = "max_eligible_at")
    private Instant maxEligibleAt;

    @Column(name = "watermark")
    private Instant watermark;

    @Column(name = "retries", nullable = false)
    private int retries;

    @Column(name = "lease_owner")
    private String leaseOwner;

    @Column(name = "lease_until")
    private Instant leaseUntil;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "error", length = 1000)
    private String error;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected DepartmentAssignmentRunEntity() {
    }

    @Builder
    private DepartmentAssignmentRunEntity(final DepartmentAssignmentRunId runId,
                                          final Long version,
                                          final DepartmentId departmentId,
                                          final CourierAssignmentDateField dateField,
                                          final Instant windowFrom,
                                          final Instant windowTo,
                                          final Instant backlogFrom,
                                          final int batchSize,
                                          final DepartmentAssignmentRunStatus status,
                                          final LocalDateTime cursorDate,
                                          final String cursorId,
                                          final Instant maxEligibleAt,
                                          final Instant watermark,
                                          final int retries,
                                          final String leaseOwner,
                                          final Instant leaseUntil,
                                          final String summary,
                                          final String error,
                                          final Instant createdAt,
                                          final Instant updatedAt) {
        this.runId = runId;
        this.version = version;
        this.departmentId = departmentId;
        this.dateField = dateField;
        this.windowFrom = windowFrom;
        this.windowTo = windowTo;
        this.backlogFrom = backlogFrom;
        this.batchSize = batchSize;
        this.status = status;
        this.cursorDate = cursorDate;
        this.cursorId = cursorId;
        this.maxEligibleAt = maxEligibleAt;
        this.watermark = watermark;
        this.retries = retries;
        this.leaseOwner = leaseOwner;
        this.leaseUntil = leaseUntil;
        this.summary = summary;
        this.error = error;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
