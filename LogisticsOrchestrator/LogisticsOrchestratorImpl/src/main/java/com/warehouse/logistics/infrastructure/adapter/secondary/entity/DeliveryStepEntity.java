package com.warehouse.logistics.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.logistics.domain.enumeration.DeliveryMethod;
import com.warehouse.logistics.domain.enumeration.DeliveryStepOutcome;
import jakarta.persistence.*;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity(name = "delivery.DeliveryStepEntity")
@Table(name = "delivery_step")
public class DeliveryStepEntity {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false))
    private DeliveryStepId id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "delivery_id", nullable = false,
            insertable = false, updatable = false))
    private DeliveryId deliveryId;

    @Column(name = "step_number", nullable = false)
    private Integer stepNumber;

    @Column(name = "attempted_at", nullable = false)
    private LocalDateTime attemptedAt;

    @Column(name = "outcome", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryStepOutcome outcome;

    @Column(name = "delivery_status")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "signature_id"))
    private SignatureId signatureId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id"))
    private UserId userId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "supplier_id"))
    private SupplierId supplierId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "department_id"))
    private DepartmentId departmentId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "vehicle_id"))
    private VehicleId vehicleId;

    @Column(name = "delivery_method")
    @Enumerated(EnumType.STRING)
    private DeliveryMethod method;

    @Column(name = "comment", length = 1000)
    private String comment;

    @Column(name = "failure_reason", length = 1000)
    private String failureReason;

    @Column(name = "token")
    private String token;

    protected DeliveryStepEntity() {
    }

    @Builder(toBuilder = true)
    private DeliveryStepEntity(final DeliveryStepId id,
                               final DeliveryId deliveryId,
                               final Integer stepNumber,
                               final LocalDateTime attemptedAt,
                               final DeliveryStepOutcome outcome,
                               final DeliveryStatus deliveryStatus,
                               final SignatureId signatureId,
                               final UserId userId,
                               final SupplierId supplierId,
                               final DepartmentId departmentId,
                               final VehicleId vehicleId,
                               final DeliveryMethod method,
                               final String comment,
                               final String failureReason,
                               final String token) {
        this.id = id == null ? DeliveryStepId.generate() : id;
        this.deliveryId = deliveryId;
        this.stepNumber = stepNumber;
        this.attemptedAt = attemptedAt;
        this.outcome = outcome;
        this.deliveryStatus = deliveryStatus;
        this.signatureId = signatureId;
        this.userId = userId;
        this.supplierId = supplierId;
        this.departmentId = departmentId;
        this.vehicleId = vehicleId;
        this.method = method;
        this.comment = comment;
        this.failureReason = failureReason;
        this.token = token;
    }

    public DeliveryStepId getId() { return id; }
    public DeliveryId getDeliveryId() { return deliveryId; }
    public Integer getStepNumber() { return stepNumber; }
    public LocalDateTime getAttemptedAt() { return attemptedAt; }
    public DeliveryStepOutcome getOutcome() { return outcome; }
    public DeliveryStatus getDeliveryStatus() { return deliveryStatus; }
    public SignatureId getSignatureId() { return signatureId; }
    public UserId getUserId() { return userId; }
    public SupplierId getSupplierId() { return supplierId; }
    public DepartmentId getDepartmentId() { return departmentId; }
    public VehicleId getVehicleId() { return vehicleId; }
    public DeliveryMethod getMethod() { return method; }
    public String getComment() { return comment; }
    public String getFailureReason() { return failureReason; }
    public String getToken() { return token; }
}
