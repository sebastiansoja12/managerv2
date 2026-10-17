package com.warehouse.returning.infrastructure.adapter.secondary.entity;

import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.DepartmentId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.OperatorId;
import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.enumeration.Status;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ReturnId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ShipmentId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.UserId;
import jakarta.persistence.*;

import java.time.Instant;

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

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "assigned_department_id"))
    private DepartmentId assignedDepartmentId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "returned_department_id"))
    private DepartmentId returnedDepartmentId;

    @AttributeOverride(name = "value", column = @Column(name = "assigned_to"))
    private UserId assignedTo;

    @AttributeOverride(name = "value", column = @Column(name = "processed_by"))
    private UserId processedBy;

    @AttributeOverride(name = "value", column = @Column(name = "reason_code"))
    @Enumerated(EnumType.STRING)
    private ReasonCode reasonCode;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "operator_id"))
    private OperatorId operatorId;

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
            final DepartmentId assignedDepartmentId,
            final DepartmentId returnedDepartmentId,
            final UserId assignedTo,
            final UserId processedBy,
            final ReasonCode reasonCode,
            final OperatorId operatorId,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        this.returnId = returnId;
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
