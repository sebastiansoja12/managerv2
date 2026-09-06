package com.warehouse.logistics.domain.model;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.logistics.domain.enumeration.DeliveryDerivationType;
import com.warehouse.logistics.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logistics.domain.enumeration.DeliveryMethod;
import com.warehouse.logistics.domain.enumeration.DeliveryType;
import com.warehouse.logistics.domain.enumeration.PickupMethod;

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
    private DepartmentId originDepartmentId;
    private DepartmentId destinationDepartmentId;
    private PickupMethod pickupMethod;
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
                    final DepartmentId originDepartmentId,
                    final DepartmentId destinationDepartmentId,
                    final PickupMethod pickupMethod,
                    final DeliveryMethod method,
                    final PickupPointId pickupPointId,
                    final PickupPointId deliveryPickupPointId,
                    final SignatureId signatureId,
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
        this.originDepartmentId = originDepartmentId;
        this.destinationDepartmentId = destinationDepartmentId;
        this.pickupMethod = pickupMethod;
        this.method = method;
        this.pickupPointId = pickupPointId;
        this.deliveryPickupPointId = deliveryPickupPointId;
        this.signatureRequired = Boolean.TRUE.equals(signatureRequired);
        this.deliverySteps.add(DeliveryStep.created(deliveryId, createdAt, signatureId,
                "DeliverableShipment created", userId));
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

    public DepartmentId getOriginDepartmentId() {
        return originDepartmentId;
    }

    public DepartmentId getDestinationDepartmentId() {
        return destinationDepartmentId;
    }

    public PickupMethod getPickupMethod() {
        return pickupMethod;
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

    public void changeDestinationDepartment(final DepartmentId destinationDepartmentId) {
        if (Objects.equals(this.destinationDepartmentId, destinationDepartmentId)) {
            return;
        }
        ensureCanBeModified();
        this.destinationDepartmentId = destinationDepartmentId;
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

    public static Delivery forPallet(final DeliveryTarget target, final UserId userId) {
        if (target.type() != DeliveryTargetType.PALLET) {
            throw new IllegalArgumentException("Pallet delivery requires pallet target");
        }
        return new Delivery(target, null, DeliveryType.OUTBOUND, null, null, null, DeliveryMethod.COURIER,
                null, null, null, false, userId);
    }

    public static Delivery derivedFrom(final Delivery source, final ShipmentId shipmentId,
                                       final DeliveryDerivationType derivationType, final Boolean signatureRequired,
                                       final UserId userId) {
        final DeliveryTarget target = DeliveryTarget.shipment(shipmentId);
        if (derivationType.reversesRoute()) {
            return new Delivery(target, shipmentId, DeliveryType.OUTBOUND, source.destinationDepartmentId,
                    source.originDepartmentId, PickupMethod.DEPARTMENT, DeliveryMethod.COURIER, null, null, null,
                    signatureRequired, userId);
        }
        return new Delivery(target, shipmentId, DeliveryType.OUTBOUND, source.originDepartmentId,
                source.destinationDepartmentId, source.pickupMethod, source.method, source.pickupPointId,
                source.deliveryPickupPointId, null, signatureRequired, userId);
    }

    public boolean isActive() {
        return status != DeliveryLifecycleStatus.CANCELED && status != DeliveryLifecycleStatus.COMPLETED;
    }

    public boolean replaceByPalletDelivery(final DeliveryId palletDeliveryId, final UserId userId) {
        if (!isActive()) {
            return false;
        }
        addStep(DeliveryStep.attempt(deliveryId, deliveryStatus, userId, supplierId, null, vehicleId,
                "Replaced by pallet delivery " + palletDeliveryId.getId(), null, deliverySteps.size()));
        markAsCanceled();
        return true;
    }

    public void markAsCanceled() {
        if (status == DeliveryLifecycleStatus.COMPLETED) {
            throw new IllegalStateException("Completed delivery cannot be canceled");
        }
        this.status = DeliveryLifecycleStatus.CANCELED;
    }

    public void markAsCompleted() {
        if (status == DeliveryLifecycleStatus.CANCELED) {
            throw new IllegalStateException("Canceled delivery cannot be completed");
        }
        this.status = DeliveryLifecycleStatus.COMPLETED;
        this.deliveryStatus = DeliveryStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
    }

    public void addStep(final DeliveryStep deliveryStep) {
        ensureCanBeModified();
        deliverySteps.add(deliveryStep);
    }

    private void ensureCanBeModified() {
        if (status == DeliveryLifecycleStatus.CANCELED || status == DeliveryLifecycleStatus.COMPLETED) {
            throw new IllegalStateException("Delivery cannot be modified when its status is " + status);
        }
    }
}
