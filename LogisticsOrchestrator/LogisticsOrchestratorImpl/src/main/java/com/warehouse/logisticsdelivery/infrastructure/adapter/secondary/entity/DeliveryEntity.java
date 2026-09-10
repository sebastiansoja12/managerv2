package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.*;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryType;
import com.warehouse.logisticsdelivery.domain.enumeration.PickupMethod;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryTargetType;
import jakarta.persistence.*;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "delivery.DeliveryEntity")
@Table(name = "delivery")
public class DeliveryEntity extends BelongsToOperator {

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

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "supplier_id"))
    private SupplierId supplierId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "vehicle_id"))
    private VehicleId vehicleId;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "delivery_status", nullable = false)
    private DeliveryStatusColumn deliveryStatus;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryLifecycleStatus status;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "origin_department_id"))
    private DepartmentId originDepartmentId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "destination_department_id"))
    private DepartmentId destinationDepartmentId;

    @Column(name = "pickup_method")
    @Enumerated(EnumType.STRING)
    private PickupMethod pickupMethod;

    @Column(name = "delivery_method")
    @Enumerated(EnumType.STRING)
    private DeliveryMethod method;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "pickup_point_id"))
    private PickupPointId pickupPointId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "delivery_pickup_point_id"))
    private PickupPointId deliveryPickupPointId;

    @Column(name = "signature_required", nullable = false)
    private Boolean signatureRequired;

    @Column(name = "delivery_instructions", length = 1000)
    private String instructions;

    @Column(name = "planned_delivery_at")
    private LocalDateTime plannedAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "token")
    private String token;

    @Column(name = "replaced_by_delivery_id")
    private String replacedByDeliveryId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_id", referencedColumnName = "id", nullable = false)
    @OrderBy("stepNumber ASC")
    private List<DeliveryStepEntity> deliverySteps;

    protected DeliveryEntity() {
    }

    @Builder(toBuilder = true)
    private DeliveryEntity(final DeliveryId id,
                           final ShipmentId shipmentId,
                           final DeliveryTargetType targetType,
                           final String targetId,
                           final DeliveryType type,
                           final SupplierId supplierId,
                           final VehicleId vehicleId,
                           final LocalDateTime created,
                           final DeliveryStatusColumn deliveryStatus,
                           final DeliveryLifecycleStatus status,
                           final DepartmentId originDepartmentId,
                           final DepartmentId destinationDepartmentId,
                           final PickupMethod pickupMethod,
                           final DeliveryMethod method,
                           final PickupPointId pickupPointId,
                           final PickupPointId deliveryPickupPointId,
                           final Boolean signatureRequired,
                           final String instructions,
                           final LocalDateTime plannedAt,
                           final LocalDateTime deliveredAt,
                           final String token,
                           final String replacedByDeliveryId,
                           final List<DeliveryStepEntity> deliverySteps) {
        this.id = id == null ? DeliveryId.generate() : id;
        this.shipmentId = shipmentId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.type = type;
        this.supplierId = supplierId;
        this.vehicleId = vehicleId;
        this.created = created;
        this.deliveryStatus = deliveryStatus;
        this.status = status;
        this.originDepartmentId = originDepartmentId;
        this.destinationDepartmentId = destinationDepartmentId;
        this.pickupMethod = pickupMethod;
        this.method = method;
        this.pickupPointId = pickupPointId;
        this.deliveryPickupPointId = deliveryPickupPointId;
        this.signatureRequired = signatureRequired;
        this.instructions = instructions;
        this.plannedAt = plannedAt;
        this.deliveredAt = deliveredAt;
        this.token = token;
        this.replacedByDeliveryId = replacedByDeliveryId;
        this.deliverySteps = deliverySteps == null ? new ArrayList<>() : new ArrayList<>(deliverySteps);
    }

    public DeliveryId getId() { return id; }
    public ShipmentId getShipmentId() { return shipmentId; }
    public DeliveryTargetType getTargetType() { return targetType; }
    public String getTargetId() { return targetId; }
    public DeliveryType getType() { return type; }
    public SupplierId getSupplierId() { return supplierId; }
    public VehicleId getVehicleId() { return vehicleId; }
    public LocalDateTime getCreated() { return created; }
    public DeliveryStatusColumn getDeliveryStatus() { return deliveryStatus; }
    public DeliveryLifecycleStatus getStatus() { return status; }
    public DepartmentId getOriginDepartmentId() { return originDepartmentId; }
    public DepartmentId getDestinationDepartmentId() { return destinationDepartmentId; }
    public PickupMethod getPickupMethod() { return pickupMethod; }
    public DeliveryMethod getMethod() { return method; }
    public PickupPointId getPickupPointId() { return pickupPointId; }
    public PickupPointId getDeliveryPickupPointId() { return deliveryPickupPointId; }
    public Boolean getSignatureRequired() { return signatureRequired; }
    public String getInstructions() { return instructions; }
    public LocalDateTime getPlannedAt() { return plannedAt; }
    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public String getToken() { return token; }
    public String getReplacedByDeliveryId() { return replacedByDeliveryId; }
    public List<DeliveryStepEntity> getDeliverySteps() {
        return deliverySteps == null ? List.of() : List.copyOf(deliverySteps);
    }
}
