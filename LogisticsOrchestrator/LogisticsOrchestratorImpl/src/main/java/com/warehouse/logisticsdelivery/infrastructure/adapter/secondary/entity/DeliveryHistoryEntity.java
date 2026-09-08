package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryHistoryEventType;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;
import com.warehouse.logisticsdelivery.domain.enumeration.PickupMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "delivery.DeliveryHistoryEntity")
@Table(name = "delivery_history")
public class DeliveryHistoryEntity extends BelongsToOperator {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "delivery_id", nullable = false)
    private String deliveryId;

    @Column(name = "event_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryHistoryEventType eventType;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "delivery_status")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryLifecycleStatus status;

    @Column(name = "origin_department_id")
    private Long originDepartmentId;

    @Column(name = "destination_department_id")
    private Long destinationDepartmentId;

    @Column(name = "pickup_method")
    @Enumerated(EnumType.STRING)
    private PickupMethod pickupMethod;

    @Column(name = "delivery_method")
    @Enumerated(EnumType.STRING)
    private DeliveryMethod method;

    @Column(name = "pickup_point_id")
    private UUID pickupPointId;

    @Column(name = "delivery_pickup_point_id")
    private UUID deliveryPickupPointId;

    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "vehicle_id")
    private Long vehicleId;

    @Column(name = "signature_required")
    private Boolean signatureRequired;

    @Column(name = "instructions")
    private String instructions;

    @Column(name = "planned_at")
    private LocalDateTime plannedAt;

    @Column(name = "step_count", nullable = false)
    private Integer stepCount;

    protected DeliveryHistoryEntity() {
    }

    @Builder
    private DeliveryHistoryEntity(final String deliveryId,
                                  final DeliveryHistoryEventType eventType,
                                  final Instant occurredAt,
                                  final Long userId,
                                  final DeliveryStatus deliveryStatus,
                                  final DeliveryLifecycleStatus status,
                                  final Long originDepartmentId,
                                  final Long destinationDepartmentId,
                                  final PickupMethod pickupMethod,
                                  final DeliveryMethod method,
                                  final UUID pickupPointId,
                                  final UUID deliveryPickupPointId,
                                  final Long supplierId,
                                  final Long vehicleId,
                                  final Boolean signatureRequired,
                                  final String instructions,
                                  final LocalDateTime plannedAt,
                                  final Integer stepCount) {
        this.id = UUID.randomUUID();
        this.deliveryId = deliveryId;
        this.eventType = eventType;
        this.occurredAt = occurredAt;
        this.userId = userId;
        this.deliveryStatus = deliveryStatus;
        this.status = status;
        this.originDepartmentId = originDepartmentId;
        this.destinationDepartmentId = destinationDepartmentId;
        this.pickupMethod = pickupMethod;
        this.method = method;
        this.pickupPointId = pickupPointId;
        this.deliveryPickupPointId = deliveryPickupPointId;
        this.supplierId = supplierId;
        this.vehicleId = vehicleId;
        this.signatureRequired = signatureRequired;
        this.instructions = instructions;
        this.plannedAt = plannedAt;
        this.stepCount = stepCount;
    }

    public UUID getId() { return id; }
    public String getDeliveryId() { return deliveryId; }
    public DeliveryHistoryEventType getEventType() { return eventType; }
    public Instant getOccurredAt() { return occurredAt; }
    public Long getUserId() { return userId; }
    public DeliveryStatus getDeliveryStatus() { return deliveryStatus; }
    public DeliveryLifecycleStatus getStatus() { return status; }
    public Long getOriginDepartmentId() { return originDepartmentId; }
    public Long getDestinationDepartmentId() { return destinationDepartmentId; }
    public PickupMethod getPickupMethod() { return pickupMethod; }
    public DeliveryMethod getMethod() { return method; }
    public UUID getPickupPointId() { return pickupPointId; }
    public UUID getDeliveryPickupPointId() { return deliveryPickupPointId; }
    public Long getSupplierId() { return supplierId; }
    public Long getVehicleId() { return vehicleId; }
    public Boolean getSignatureRequired() { return signatureRequired; }
    public String getInstructions() { return instructions; }
    public LocalDateTime getPlannedAt() { return plannedAt; }
    public Integer getStepCount() { return stepCount; }
}
