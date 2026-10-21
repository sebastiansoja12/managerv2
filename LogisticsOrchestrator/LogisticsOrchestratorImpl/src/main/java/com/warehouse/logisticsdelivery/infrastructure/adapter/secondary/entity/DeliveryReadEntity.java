package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryTargetType;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryType;
import jakarta.persistence.*;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity(name = "delivery.DeliveryReadEntity")
@Table(name = "delivery_rd")
public class DeliveryReadEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "id", column = @Column(name = "id", nullable = false))
    private DeliveryId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "shipment_id"))
    private ShipmentId shipmentId;

    @Column(name = "target_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryTargetType targetType;

    @Column(name = "target_id", nullable = false)
    private String targetId;

    @Column(name = "delivery_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryType type;

    @Column(name = "delivery_status")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryLifecycleStatus status;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "origin_department_id"))
    private DepartmentId originDepartmentId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "destination_department_id"))
    private DepartmentId destinationDepartmentId;

    @Column(name = "delivery_method")
    @Enumerated(EnumType.STRING)
    private DeliveryMethod method;

    @Column(name = "step_count", nullable = false)
    private Integer stepCount;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    protected DeliveryReadEntity() {
    }

    @Builder
    private DeliveryReadEntity(final DeliveryId id,
                               final ShipmentId shipmentId,
                               final DeliveryTargetType targetType,
                               final String targetId,
                               final DeliveryType type,
                               final DeliveryStatus deliveryStatus,
                               final DeliveryLifecycleStatus status,
                               final DepartmentId originDepartmentId,
                               final DepartmentId destinationDepartmentId,
                               final DeliveryMethod method,
                               final Integer stepCount,
                               final LocalDateTime created,
                               final LocalDateTime deliveredAt) {
        this.id = id;
        this.shipmentId = shipmentId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.type = type;
        this.deliveryStatus = deliveryStatus;
        this.status = status;
        this.originDepartmentId = originDepartmentId;
        this.destinationDepartmentId = destinationDepartmentId;
        this.method = method;
        this.stepCount = stepCount;
        this.created = created;
        this.deliveredAt = deliveredAt;
    }

    public DeliveryId getId() { return id; }
    public ShipmentId getShipmentId() { return shipmentId; }
    public DeliveryTargetType getTargetType() { return targetType; }
    public String getTargetId() { return targetId; }
    public DeliveryType getType() { return type; }
    public DeliveryStatus getDeliveryStatus() { return deliveryStatus; }
    public DeliveryLifecycleStatus getStatus() { return status; }
    public DepartmentId getOriginDepartmentId() { return originDepartmentId; }
    public DepartmentId getDestinationDepartmentId() { return destinationDepartmentId; }
    public DeliveryMethod getMethod() { return method; }
    public Integer getStepCount() { return stepCount; }
    public LocalDateTime getCreated() { return created; }
    public LocalDateTime getDeliveredAt() { return deliveredAt; }
}
