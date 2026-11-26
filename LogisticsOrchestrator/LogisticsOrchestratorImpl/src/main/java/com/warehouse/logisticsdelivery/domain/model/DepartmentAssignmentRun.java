package com.warehouse.logisticsdelivery.domain.model;

import com.warehouse.commonassets.identificator.DepartmentAssignmentRunId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.logisticsdelivery.domain.enumeration.CourierAssignmentDateField;
import com.warehouse.logisticsdelivery.domain.enumeration.DepartmentAssignmentRunStatus;
import com.warehouse.logisticsdelivery.domain.exception.DepartmentAssignmentRunStateException;
import com.warehouse.logisticsdelivery.domain.vo.AssignmentRunSummary;
import com.warehouse.logisticsdelivery.domain.vo.AssignmentWindow;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryAssignmentCursor;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentAssignmentRunSnapshot;

import java.time.Duration;
import java.time.Instant;

public class DepartmentAssignmentRun {

    private static final int MAX_ERROR_LENGTH = 1000;

    private final DepartmentAssignmentRunId runId;
    private Long version;
    private final DepartmentId departmentId;
    private final CourierAssignmentDateField dateField;
    private final AssignmentWindow window;
    private final int batchSize;
    private final Instant createdAt;
    private DepartmentAssignmentRunStatus status;
    private DeliveryAssignmentCursor cursor;
    private Instant maxEligibleAt;
    private Instant watermark;
    private int retries;
    private String leaseOwner;
    private Instant leaseUntil;
    private AssignmentRunSummary summary;
    private String error;
    private Instant updatedAt;

    public DepartmentAssignmentRun(final DepartmentAssignmentRunId runId,
                                   final Long version,
                                   final DepartmentId departmentId,
                                   final CourierAssignmentDateField dateField,
                                   final AssignmentWindow window,
                                   final int batchSize,
                                   final DepartmentAssignmentRunStatus status,
                                   final DeliveryAssignmentCursor cursor,
                                   final Instant maxEligibleAt,
                                   final Instant watermark,
                                   final int retries,
                                   final String leaseOwner,
                                   final Instant leaseUntil,
                                   final AssignmentRunSummary summary,
                                   final String error,
                                   final Instant createdAt,
                                   final Instant updatedAt) {
        this.runId = runId;
        this.version = version;
        this.departmentId = departmentId;
        this.dateField = dateField;
        this.window = window;
        this.batchSize = batchSize;
        this.status = status;
        this.cursor = cursor;
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

    public void recordBatch(final AssignmentRunSummary batch, final DeliveryAssignmentCursor nextCursor,
                            final Instant batchMaxEligibleAt, final Instant now, final Duration lease) {
        ensureProcessing();
        this.summary = this.summary.plus(batch);
        this.cursor = nextCursor;
        if (batchMaxEligibleAt != null && (this.maxEligibleAt == null || batchMaxEligibleAt.isAfter(this.maxEligibleAt))) {
            this.maxEligibleAt = batchMaxEligibleAt;
        }
        this.leaseUntil = now.plus(lease);
        this.updatedAt = now;
    }

    public void complete(final Instant now) {
        if (this.status == DepartmentAssignmentRunStatus.COMPLETED) {
            return;
        }
        ensureProcessing();
        this.status = DepartmentAssignmentRunStatus.COMPLETED;
        this.watermark = this.maxEligibleAt != null ? this.maxEligibleAt : this.window.to();
        releaseLease(now);
    }

    public void stop(final String reason, final Instant now) {
        ensureProcessing();
        this.status = DepartmentAssignmentRunStatus.FAILED;
        this.error = truncate(reason);
        releaseLease(now);
    }

    public void registerError(final String cause, final Instant now, final int maxRetries) {
        ensureProcessing();
        this.retries++;
        this.error = truncate(cause);
        this.status = this.retries >= maxRetries ? DepartmentAssignmentRunStatus.ERROR
                : DepartmentAssignmentRunStatus.PENDING;
        releaseLease(now);
    }

    public void applyPersistedVersion(final Long persistedVersion) {
        this.version = persistedVersion;
    }

    public boolean isLeasedBy(final String owner) {
        return this.status == DepartmentAssignmentRunStatus.PROCESSING && owner.equals(this.leaseOwner);
    }

    public DepartmentAssignmentRunSnapshot snapshot() {
        return new DepartmentAssignmentRunSnapshot(runId, version, departmentId, dateField, window, batchSize, status,
                cursor, maxEligibleAt, watermark, retries, leaseOwner, leaseUntil, summary, error, createdAt,
                updatedAt);
    }

    public DepartmentAssignmentRunId getRunId() {
        return runId;
    }

    public DepartmentId getDepartmentId() {
        return departmentId;
    }

    public CourierAssignmentDateField getDateField() {
        return dateField;
    }

    public AssignmentWindow getWindow() {
        return window;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public DepartmentAssignmentRunStatus getStatus() {
        return status;
    }

    public DeliveryAssignmentCursor getCursor() {
        return cursor;
    }

    public AssignmentRunSummary getSummary() {
        return summary;
    }

    public int getRetries() {
        return retries;
    }

    private void releaseLease(final Instant now) {
        this.leaseOwner = null;
        this.leaseUntil = null;
        this.updatedAt = now;
    }

    private void ensureProcessing() {
        if (this.status != DepartmentAssignmentRunStatus.PROCESSING) {
            throw new DepartmentAssignmentRunStateException("Department assignment run " + runId.value()
                    + " is not processing but " + status);
        }
    }

    private static String truncate(final String message) {
        return message == null || message.length() <= MAX_ERROR_LENGTH ? message : message.substring(0, MAX_ERROR_LENGTH);
    }
}
