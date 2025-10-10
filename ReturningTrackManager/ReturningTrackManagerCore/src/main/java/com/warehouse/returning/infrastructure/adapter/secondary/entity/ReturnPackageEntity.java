package com.warehouse.returning.infrastructure.adapter.secondary.entity;

import java.time.Instant;

import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.enumeration.Status;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.DepartmentCode;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ReturnId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ShipmentId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.UserId;

import jakarta.persistence.*;

@Entity
@Table(name = "returning_return_package")
public class ReturnPackageEntity {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "return_id"))
    private ReturnId returnId;

    @AttributeOverride(name = "value", column = @Column(name = "shipment_id"))
    private ShipmentId shipmentId;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "return_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status returnStatus;

    @AttributeOverride(name = "value", column = @Column(name = "return_token"))
    private ReturnToken returnToken;

    @AttributeOverride(name = "value", column = @Column(name = "assigned_department"))
    private DepartmentCode assignedDepartmentCode;

    @AttributeOverride(name = "value", column = @Column(name = "returned_department"))
    private DepartmentCode returnedDepartmentCode;

    @AttributeOverride(name = "value", column = @Column(name = "assigned_to"))
    private UserId assignedTo;

    @AttributeOverride(name = "value", column = @Column(name = "processed_by"))
    private UserId processedBy;

    @AttributeOverride(name = "value", column = @Column(name = "reason_code"))
    @Enumerated(EnumType.STRING)
    private ReasonCode reasonCode;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public ReturnPackageEntity() {

    }

    public ReturnPackageEntity(
            final ReturnId returnId,
            final ShipmentId shipmentId,
            final String reason,
            final Status returnStatus,
            final ReturnToken returnToken,
            final DepartmentCode assignedDepartmentCode,
            final DepartmentCode returnedDepartmentCode,
            final UserId assignedTo,
            final UserId processedBy,
            final ReasonCode reasonCode,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        this.returnId = returnId;
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

    public ReturnId getReturnId() {
        return returnId;
    }

    public Status getReturnStatus() {
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
