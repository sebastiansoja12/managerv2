package com.warehouse.logistics.domain.model;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.logistics.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logistics.domain.enumeration.DeliveryMethod;
import com.warehouse.logistics.domain.enumeration.DeliveryStatus;
import com.warehouse.logistics.domain.enumeration.DeliveryType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Delivery {
    private final DeliveryId deliveryId;
    private final DeliveryTarget target;
    private final ShipmentId shipmentId;
    private final DeliveryType type;
    private final List<DeliveryStep> deliverySteps;
    private final LocalDateTime createdAt;
    private SupplierId supplierId;
    private VehicleId vehicleId;
    private DeliveryStatus deliveryStatus;
    private DeliveryLifecycleStatus status;
    private DeliveryMethod method;
    private PickupPointId pickupPointId;
    private PickupPointId deliveryPickupPointId;
    private Boolean signatureRequired;
    private String instructions;
    private LocalDateTime plannedAt;
    private LocalDateTime deliveredAt;
    private String token;

    public Delivery(final DeliveryTarget target,
                    final ShipmentId shipmentId,
                    final DeliveryType type,
                    final DeliveryMethod method,
                    final PickupPointId pickupPointId,
                    final PickupPointId deliveryPickupPointId,
                    final ShipmentId signatureId,
                    final Boolean signatureRequired,
                    final UserId userId) {
        this.deliveryId = DeliveryId.generate();
        this.target = target;
        this.shipmentId = shipmentId;
        this.type = type;
        this.deliverySteps = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.deliveryStatus = DeliveryStatus.DEPOT;
        this.status = DeliveryLifecycleStatus.CREATED;
        this.method = method;
        this.pickupPointId = pickupPointId;
        this.deliveryPickupPointId = deliveryPickupPointId;
        this.signatureRequired = Boolean.TRUE.equals(signatureRequired);
        this.deliverySteps.add(DeliveryStep.created(deliveryId, createdAt, signatureId, method,
                "Shipment created", userId));
    }

    public Delivery(final DeliveryId deliveryId,
                    final DeliveryTarget target,
                    final ShipmentId shipmentId,
                    final DeliveryType type,
                    final List<DeliveryStep> deliverySteps,
                    final LocalDateTime createdAt,
                    final SupplierId supplierId,
                    final VehicleId vehicleId,
                    final DeliveryStatus deliveryStatus,
                    final DeliveryLifecycleStatus status,
                    final DeliveryMethod method,
                    final PickupPointId pickupPointId,
                    final PickupPointId deliveryPickupPointId,
                    final Boolean signatureRequired,
                    final String instructions,
                    final LocalDateTime plannedAt,
                    final LocalDateTime deliveredAt,
                    final String token) {
        this.deliveryId = deliveryId;
        this.target = target;
        this.shipmentId = shipmentId;
        this.type = type;
        this.deliverySteps = new ArrayList<>(deliverySteps);
        this.createdAt = createdAt;
        this.supplierId = supplierId;
        this.vehicleId = vehicleId;
        this.deliveryStatus = deliveryStatus;
        this.status = status;
        this.method = method;
        this.pickupPointId = pickupPointId;
        this.deliveryPickupPointId = deliveryPickupPointId;
        this.signatureRequired = signatureRequired;
        this.instructions = instructions;
        this.plannedAt = plannedAt;
        this.deliveredAt = deliveredAt;
        this.token = token;
    }

    public List<DeliveryStep> getDeliverySteps() {
        return List.copyOf(deliverySteps);
    }

    public DeliveryId getDeliveryId() {
        return deliveryId;
    }

    public DeliveryTarget getTarget() {
        return target;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public DeliveryType getType() {
        return type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public SupplierId getSupplierId() {
        return supplierId;
    }

    public VehicleId getVehicleId() {
        return vehicleId;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public DeliveryLifecycleStatus getStatus() {
        return status;
    }

    public DeliveryMethod getMethod() {
        return method;
    }

    public PickupPointId getPickupPointId() {
        return pickupPointId;
    }

    public PickupPointId getDeliveryPickupPointId() {
        return deliveryPickupPointId;
    }

    public Boolean getSignatureRequired() {
        return signatureRequired;
    }

    public String getInstructions() {
        return instructions;
    }

    public LocalDateTime getPlannedAt() {
        return plannedAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public String getToken() {
        return token;
    }

    public void changeMethod(final DeliveryMethod method) {
        if (Objects.equals(this.method, method)) {
            return;
        }
        ensureCanBeModified();
        this.method = method;
        markAsModified();
    }

    public void changePickupPointId(final PickupPointId pickupPointId) {
        if (Objects.equals(this.pickupPointId, pickupPointId)) {
            return;
        }
        ensureCanBeModified();
        this.pickupPointId = pickupPointId;
        markAsModified();
    }

    public void changeDeliveryPickupPointId(final PickupPointId pickupPointId) {
        if (Objects.equals(this.deliveryPickupPointId, pickupPointId)) {
            return;
        }
        ensureCanBeModified();
        this.deliveryPickupPointId = pickupPointId;
        markAsModified();
    }

    public void changeSignatureRequired(final Boolean signatureRequired) {
        if (Objects.equals(this.signatureRequired, signatureRequired)) {
            return;
        }
        ensureCanBeModified();
        this.signatureRequired = signatureRequired;
        markAsModified();
    }

    private void markAsModified() {
        ensureCanBeModified();
        this.status = DeliveryLifecycleStatus.MODIFIED;
    }

    public void markAsCanceled() {
        if (status == DeliveryLifecycleStatus.COMPLETED) {
            throw new IllegalStateException("Completed delivery cannot be canceled");
        }
        this.status = DeliveryLifecycleStatus.CANCELED;
    }

    private void markAsCompleted() {
        if (status == DeliveryLifecycleStatus.CANCELED) {
            throw new IllegalStateException("Canceled delivery cannot be completed");
        }
        this.status = DeliveryLifecycleStatus.COMPLETED;
        this.deliveryStatus = DeliveryStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
    }

    public void addStep(final DeliveryStep deliveryStep) {
        ensureCanBeModified();
        if (!deliveryId.equals(deliveryStep.deliveryId())) {
            throw new IllegalArgumentException("Delivery step belongs to another delivery");
        }
        if (deliveryStep.stepNumber() != deliverySteps.size() + 1) {
            throw new IllegalArgumentException("Delivery step number must follow the existing steps");
        }
        deliverySteps.add(deliveryStep);
        if (deliveryStep.deliveryStatus() != null) {
            this.deliveryStatus = deliveryStep.deliveryStatus();
        }
        this.supplierId = deliveryStep.supplierId();
        this.vehicleId = deliveryStep.vehicleId();
        if (deliveryStep.token() != null) {
            this.token = deliveryStep.token();
        }
        if (deliveryStep.deliveryStatus() == DeliveryStatus.DELIVERED) {
            markAsCompleted();
        } else if (deliverySteps.size() > 1) {
            markAsModified();
        }
    }

    private void ensureCanBeModified() {
        if (status == DeliveryLifecycleStatus.CANCELED || status == DeliveryLifecycleStatus.COMPLETED) {
            throw new IllegalStateException("Delivery cannot be modified when its status is " + status);
        }
    }
}
