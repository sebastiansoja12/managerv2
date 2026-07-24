package com.warehouse.returning.domain.model;

import com.warehouse.commonassets.enumeration.ReturnStatus;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ReturnToken;
import com.warehouse.returning.domain.enumeration.ReasonCode;

import java.time.Instant;
import java.util.Objects;

public class ReturnPackage {
    private ReturnPackageId returnPackageId;
    private ShipmentId shipmentId;
    private String reason;
    private ReturnStatus returnStatus;
    private ReturnToken returnToken;
    private DepartmentId assignedDepartmentId;
    private DepartmentId returnedDepartmentId;
    private UserId assignedTo;
    private UserId processedBy;
    private OperatorId operatorId;
    private ReasonCode reasonCode;
    private Instant createdAt;
    private Instant updatedAt;

	public ReturnPackage(final DepartmentId assignedDepartmentId, final UserId assignedTo,
			final OperatorId operatorId, final UserId processedBy, final String reason, final ReasonCode reasonCode,
			final DepartmentId returnedDepartmentId, final ReturnPackageId returnPackageId,
            final ReturnToken returnToken, final ShipmentId shipmentId) {
        validateCreationData(assignedDepartmentId, assignedTo, reason, reasonCode, shipmentId);
        this.assignedDepartmentId = assignedDepartmentId;
        this.assignedTo = assignedTo;
        this.operatorId = operatorId;
        this.processedBy = processedBy;
        this.reason = reason;
        this.reasonCode = reasonCode;
        this.returnedDepartmentId = returnedDepartmentId;
        this.returnPackageId = returnPackageId;
        this.returnStatus = ReturnStatus.CREATED;
        this.returnToken = returnToken;
        this.shipmentId = shipmentId;
        this.updatedAt = Instant.now();
        this.createdAt = Instant.now();
    }

    private void validateCreationData(final DepartmentId assignedDepartmentId,
                                      final UserId assignedTo,
                                      final String reason,
                                      final ReasonCode reasonCode,
                                      final ShipmentId shipmentId) {
        Objects.requireNonNull(assignedDepartmentId, "Assigned department ID is required");
        Objects.requireNonNull(assignedTo, "Assigned user is required");
        Objects.requireNonNull(reasonCode, "Reason code is required");
        Objects.requireNonNull(shipmentId, "Shipment id is required");
        if (assignedDepartmentId.value() == null) {
            throw new IllegalArgumentException("Assigned department ID is required");
        }
        if (assignedTo.value() == null) {
            throw new IllegalArgumentException("Assigned user is required");
        }
        if (shipmentId.getValue() == null) {
            throw new IllegalArgumentException("Shipment id is required");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Reason is required");
        }
    }

    public void startProcessing(final UserId processedBy, final Instant currentTime) {
        if (this.returnStatus != ReturnStatus.CREATED) {
            throw new IllegalArgumentException("Cannot process this return process");
        }
        this.returnStatus = ReturnStatus.PROCESSING;
        this.processedBy = processedBy;
        this.updatedAt = currentTime;
    }

    public void markAsCanceled(final UserId processedBy) {
        this.returnStatus = ReturnStatus.CANCELLED;
        this.processedBy = processedBy;
        markAsModified();
    }

    private void markAsModified() {
        this.updatedAt = Instant.now();
    }

    public DepartmentId getAssignedDepartmentId() {
        return assignedDepartmentId;
    }

    public UserId getAssignedTo() {
        return assignedTo;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public OperatorId getOperatorId() {
        return operatorId;
    }

    public UserId getProcessedBy() {
        return processedBy;
    }

    public String getReason() {
        return reason;
    }

    public ReasonCode getReasonCode() {
        return reasonCode;
    }

    public DepartmentId getReturnedDepartmentId() {
        return returnedDepartmentId;
    }

    public ReturnPackageId getReturnPackageId() {
        return returnPackageId;
    }

    public ReturnStatus getReturnStatus() {
        return returnStatus;
    }

    public ReturnToken getReturnToken() {
        return returnToken;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
