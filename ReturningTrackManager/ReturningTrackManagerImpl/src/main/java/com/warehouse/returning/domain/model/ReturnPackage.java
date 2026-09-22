package com.warehouse.returning.domain.model;



import com.warehouse.common.DepartmentId;
import com.warehouse.common.OperatorId;
import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.event.ReturnPackageProcessingStarted;
import com.warehouse.returning.domain.exception.StatusChangeException;
import com.warehouse.returning.domain.vo.*;

import java.time.Instant;

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
    private ReasonCode reasonCode;
    private OperatorId operatorId;
    private Instant createdAt;
    private Instant updatedAt;

    public ReturnPackage(final ReturnPackageId returnPackageId,
                         final ShipmentId shipmentId,
                         final String reason,
                         final ReturnToken returnToken,
                         final DepartmentId assignedDepartmentId,
                         final DepartmentId returnedDepartmentId,
                         final UserId assignedTo,
                         final UserId processedBy,
                         final ReasonCode reasonCode) {
        this(returnPackageId, shipmentId, reason, returnToken, assignedDepartmentId,
                returnedDepartmentId, assignedTo, processedBy, reasonCode, null);
    }

    public ReturnPackage(final ReturnPackageId returnPackageId,
                         final ShipmentId shipmentId,
                         final String reason,
                         final ReturnToken returnToken,
                         final DepartmentId assignedDepartmentId,
                         final DepartmentId returnedDepartmentId,
                         final UserId assignedTo,
                         final UserId processedBy,
                         final ReasonCode reasonCode,
                         final OperatorId operatorId) {
        this.returnPackageId = returnPackageId;
        this.shipmentId = shipmentId;
        this.reason = reason;
        this.returnStatus = ReturnStatus.CREATED;
        this.returnToken = returnToken;
        this.assignedDepartmentId = assignedDepartmentId;
        this.returnedDepartmentId = returnedDepartmentId;
        this.assignedTo = assignedTo;
        this.processedBy = processedBy;
        this.reasonCode = reasonCode;
        this.operatorId = operatorId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public ReturnPackage(final ReturnPackageId returnPackageId,
                         final ShipmentId shipmentId,
                         final String reason,
                         final ReturnStatus returnStatus,
                         final ReturnToken returnToken,
                         final DepartmentId assignedDepartmentId,
                         final DepartmentId returnedDepartmentId,
                         final UserId assignedTo,
                         final UserId processedBy,
                         final ReasonCode reasonCode,
                         final OperatorId operatorId,
                         final Instant createdAt,
                         final Instant updatedAt) {
        this.returnPackageId = returnPackageId;
        this.shipmentId = shipmentId;
        this.reason = reason;
        this.returnStatus = returnStatus;
        this.returnToken = returnToken;
        this.assignedDepartmentId = assignedDepartmentId;
        this.returnedDepartmentId = returnedDepartmentId;
        this.assignedTo = assignedTo;
        this.processedBy = processedBy;
        this.reasonCode = reasonCode;
        this.operatorId = operatorId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public UserId getProcessedBy() {
        return processedBy;
    }

    public String getReason() {
        return reason;
    }

    public ReasonCode getReasonCode() {
        return reasonCode;
    }

    public OperatorId getOperatorId() {
        return operatorId;
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

    public void markAsCanceled() {
        if (this.returnStatus == ReturnStatus.COMPLETED) {
            throw new StatusChangeException("Return package is already completed, cannot override status");
        }
        changeReturnStatus(ReturnStatus.CANCELLED);
        markAsModified();
    }

    public ReturnPackageProcessingStarted markAsProcessing() {
        if (this.returnStatus != ReturnStatus.CREATED) {
            throw new StatusChangeException("Only a created return package can start processing");
        }
        changeReturnStatus(ReturnStatus.PROCESSING);
        markAsModified();
        return new ReturnPackageProcessingStarted(toSnapshot(), updatedAt);
    }

    public void markAsCompleted() {
        if (this.returnStatus == ReturnStatus.CANCELLED) {
            throw new StatusChangeException("Return package is already cancelled, cannot override status");
        } else if (this.returnStatus == ReturnStatus.COMPLETED) {
            throw new StatusChangeException("Return package is already completed, cannot override status");
        } else if (this.returnStatus != ReturnStatus.PROCESSING) {
            throw new StatusChangeException("Only a processing return package can be completed");
        }
        changeReturnStatus(ReturnStatus.COMPLETED);
        markAsModified();
    }

    private void changeReturnStatus(final ReturnStatus returnStatus) {
        this.returnStatus = returnStatus;
    }

    private void markAsModified() {
        this.updatedAt = Instant.now();
    }

    public ReturnPackageSnapshot toSnapshot() {
        return new ReturnPackageSnapshot(
                returnPackageId,
                shipmentId,
                reason,
                returnStatus,
                returnToken,
                assignedDepartmentId,
                returnedDepartmentId,
                assignedTo,
                processedBy,
                reasonCode,
                operatorId,
                createdAt,
                updatedAt
        );
    }

    public boolean isSupplier() {
        return this.assignedTo.isSupplier();
    }

    public ReturnPackage(final ReturnPackageBuilder builder) {
        this.returnPackageId = builder.returnPackageId;
        this.shipmentId = builder.shipmentId;
        this.reason = builder.reason;
        this.returnStatus = builder.returnStatus;
        this.returnToken = builder.returnToken;
        this.assignedDepartmentId = builder.assignedDepartmentId;
        this.returnedDepartmentId = builder.returnedDepartmentId;
        this.assignedTo = builder.assignedTo;
        this.processedBy = builder.processedBy;
        this.reasonCode = builder.reasonCode;
        this.operatorId = builder.operatorId;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static ReturnPackageBuilder builder() {
        return new ReturnPackageBuilder();
    }

    public void changeReasonCode(final ReasonCode reasonCode) {
        this.reasonCode = reasonCode;
        markAsModified();
    }

    public static class ReturnPackageBuilder {
        private ReturnPackageId returnPackageId;
        private ShipmentId shipmentId;
        private String reason;
        private ReturnStatus returnStatus;
        private ReturnToken returnToken;
        private DepartmentId assignedDepartmentId;
        private DepartmentId returnedDepartmentId;
        private UserId assignedTo;
        private UserId processedBy;
        private ReasonCode reasonCode;
        private OperatorId operatorId;
        private Instant createdAt;
        private Instant updatedAt;

        public ReturnPackageBuilder returnPackageId(ReturnPackageId returnPackageId) {
            this.returnPackageId = returnPackageId;
            return this;
        }

        public ReturnPackageBuilder shipmentId(ShipmentId shipmentId) {
            this.shipmentId = shipmentId;
            return this;
        }

        public ReturnPackageBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public ReturnPackageBuilder returnStatus(ReturnStatus returnStatus) {
            this.returnStatus = returnStatus;
            return this;
        }

        public ReturnPackageBuilder returnToken(ReturnToken returnToken) {
            this.returnToken = returnToken;
            return this;
        }

        public ReturnPackageBuilder assignedDepartmentId(final DepartmentId assignedDepartmentId) {
            this.assignedDepartmentId = assignedDepartmentId;
            return this;
        }

        public ReturnPackageBuilder returnedDepartmentId(final DepartmentId returnedDepartmentId) {
            this.returnedDepartmentId = returnedDepartmentId;
            return this;
        }

        public ReturnPackageBuilder assignedTo(UserId assignedTo) {
            this.assignedTo = assignedTo;
            return this;
        }

        public ReturnPackageBuilder processedBy(UserId processedBy) {
            this.processedBy = processedBy;
            return this;
        }

        public ReturnPackageBuilder reasonCode(ReasonCode reasonCode) {
            this.reasonCode = reasonCode;
            return this;
        }

        public ReturnPackageBuilder operatorId(final OperatorId operatorId) {
            this.operatorId = operatorId;
            return this;
        }

        public ReturnPackageBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ReturnPackageBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ReturnPackage build() {
            return new ReturnPackage(this);
        }
    }
}
