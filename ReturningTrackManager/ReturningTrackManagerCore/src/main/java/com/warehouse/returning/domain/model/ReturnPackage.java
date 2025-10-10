package com.warehouse.returning.domain.model;


import java.time.Instant;

import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.event.ReturnPackageCanceled;
import com.warehouse.returning.domain.event.ReturnPackageCompleted;
import com.warehouse.returning.domain.event.ReturnPackageCreated;
import com.warehouse.returning.domain.registry.DomainRegistry;
import com.warehouse.returning.domain.vo.*;

public class ReturnPackage {
    private ReturnPackageId returnPackageId;
    private ShipmentId shipmentId;
    private String reason;
    private ReturnStatus returnStatus;
    private ReturnToken returnToken;
    private DepartmentCode assignedDepartmentCode;
    private DepartmentCode returnedDepartmentCode;
    private UserId assignedTo;
    private UserId processedBy;
    private ReasonCode reasonCode;
    private Instant createdAt;
    private Instant updatedAt;

    public ReturnPackage(final ReturnPackageId returnPackageId,
                         final ShipmentId shipmentId,
                         final String reason,
                         final ReturnToken returnToken,
                         final DepartmentCode assignedDepartmentCode,
                         final DepartmentCode returnedDepartmentCode,
                         final UserId assignedTo,
                         final UserId processedBy,
                         final ReasonCode reasonCode) {
        this.returnPackageId = returnPackageId;
        this.shipmentId = shipmentId;
        this.reason = reason;
        this.returnStatus = ReturnStatus.CREATED;
        this.returnToken = returnToken;
        this.assignedDepartmentCode = assignedDepartmentCode;
        this.returnedDepartmentCode = returnedDepartmentCode;
        this.assignedTo = assignedTo;
        this.processedBy = processedBy;
        this.reasonCode = reasonCode;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        DomainRegistry.publish(new ReturnPackageCreated(this.toSnapshot(), Instant.now()));
    }

    public ReturnPackage(final ReturnPackageId returnPackageId,
                         final ShipmentId shipmentId,
                         final String reason,
                         final ReturnStatus returnStatus,
                         final ReturnToken returnToken,
                         final DepartmentCode assignedDepartmentCode,
                         final DepartmentCode returnedDepartmentCode,
                         final UserId assignedTo,
                         final UserId processedBy,
                         final ReasonCode reasonCode,
                         final Instant createdAt,
                         final Instant updatedAt) {
        this.returnPackageId = returnPackageId;
        this.shipmentId = shipmentId;
        this.reason = reason;
        this.returnStatus = returnStatus;
        this.returnToken = returnToken;
        this.assignedDepartmentCode = assignedDepartmentCode;
        this.returnedDepartmentCode = returnedDepartmentCode;
        this.assignedTo = assignedTo;
        this.processedBy = processedBy;
        this.reasonCode = reasonCode;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public DepartmentCode getAssignedDepartmentCode() {
        return assignedDepartmentCode;
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

    public DepartmentCode getReturnedDepartmentCode() {
        return returnedDepartmentCode;
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

    public void markAsDeleted() {
        changeReturnStatus(ReturnStatus.CANCELLED);
        markAsModified();
        DomainRegistry.publish(new ReturnPackageCanceled(this.toSnapshot(), Instant.now()));
    }

    public void markAsCompleted() {
        changeReturnStatus(ReturnStatus.COMPLETED);
        markAsModified();
        DomainRegistry.publish(new ReturnPackageCompleted(this.toSnapshot(), Instant.now()));
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
                assignedDepartmentCode,
                returnedDepartmentCode,
                assignedTo,
                processedBy,
                reasonCode,
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
        this.assignedDepartmentCode = builder.assignedDepartmentCode;
        this.returnedDepartmentCode = builder.returnedDepartmentCode;
        this.assignedTo = builder.assignedTo;
        this.processedBy = builder.processedBy;
        this.reasonCode = builder.reasonCode;
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
        private DepartmentCode assignedDepartmentCode;
        private DepartmentCode returnedDepartmentCode;
        private UserId assignedTo;
        private UserId processedBy;
        private ReasonCode reasonCode;
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

        public ReturnPackageBuilder assignedDepartmentCode(DepartmentCode assignedDepartmentCode) {
            this.assignedDepartmentCode = assignedDepartmentCode;
            return this;
        }

        public ReturnPackageBuilder returnedDepartmentCode(DepartmentCode returnedDepartmentCode) {
            this.returnedDepartmentCode = returnedDepartmentCode;
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
