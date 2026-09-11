package com.warehouse.logisticsdelivery.domain.model;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryDerivationType;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryTargetType;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryType;
import com.warehouse.logisticsdelivery.domain.enumeration.PickupMethod;
import com.warehouse.logisticsdelivery.domain.exception.DeliveryModificationException;
import com.warehouse.logisticsdelivery.domain.exception.DeliveryStepNotFoundException;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryTarget;

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
    private DeliveryId replacedByDeliveryId;

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
                    final String token,
                    final DeliveryId replacedByDeliveryId) {
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
        this.replacedByDeliveryId = replacedByDeliveryId;
    }

    public DeliveryId getReplacedByDeliveryId() {
        return replacedByDeliveryId;
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

    public boolean assignSupplier(final SupplierId supplierId, final VehicleId vehicleId) {
        if (Objects.equals(this.supplierId, supplierId) && Objects.equals(this.vehicleId, vehicleId)) {
            return false;
        }
        ensureCanBeModified();
        this.supplierId = supplierId;
        this.vehicleId = vehicleId;
        markAsModified();
        return true;
    }

    public boolean unassignSupplier() {
        return assignSupplier(null, null);
    }

    public boolean changeRoute(final DepartmentId originDepartmentId,
                               final DepartmentId destinationDepartmentId,
                               final PickupMethod pickupMethod,
                               final PickupPointId pickupPointId,
                               final PickupPointId deliveryPickupPointId) {
        if (Objects.equals(this.originDepartmentId, originDepartmentId)
                && Objects.equals(this.destinationDepartmentId, destinationDepartmentId)
                && Objects.equals(this.pickupMethod, pickupMethod)
                && Objects.equals(this.pickupPointId, pickupPointId)
                && Objects.equals(this.deliveryPickupPointId, deliveryPickupPointId)) {
            return false;
        }
        ensureCanBeModified();
        this.originDepartmentId = originDepartmentId;
        this.destinationDepartmentId = destinationDepartmentId;
        this.pickupMethod = pickupMethod;
        this.pickupPointId = pickupPointId;
        this.deliveryPickupPointId = deliveryPickupPointId;
        markAsModified();
        return true;
    }

    public boolean changeDestinationDepartment(final DepartmentId destinationDepartmentId) {
        return changeRoute(originDepartmentId, destinationDepartmentId, pickupMethod, pickupPointId,
                deliveryPickupPointId);
    }

    public boolean changeConditions(final DeliveryMethod method,
                                    final Boolean signatureRequired,
                                    final String instructions,
                                    final LocalDateTime plannedAt) {
        if (method == null) {
            throw new DeliveryModificationException("Delivery method is required");
        }
        if (Objects.equals(this.method, method)
                && Objects.equals(this.signatureRequired, signatureRequired)
                && Objects.equals(this.instructions, instructions)
                && Objects.equals(this.plannedAt, plannedAt)) {
            return false;
        }
        ensureCanBeModified();
        this.method = method;
        this.signatureRequired = Boolean.TRUE.equals(signatureRequired);
        this.instructions = instructions;
        this.plannedAt = plannedAt;
        markAsModified();
        return true;
    }

    public boolean commentStep(final int stepNumber, final String comment) {
        return step(stepNumber).changeComment(comment);
    }

    public boolean correctStepStatus(final int stepNumber, final DeliveryStatus correctedStatus) {
        ensureCanBeModified();
        return markAsModifiedWhen(step(stepNumber).correctStatus(correctedStatus));
    }

    public boolean attachStepSignature(final int stepNumber, final SignatureId signatureId) {
        ensureCanBeModified();
        return markAsModifiedWhen(step(stepNumber).attachSignature(signatureId));
    }

    public boolean reassignStepCarrier(final int stepNumber, final SupplierId supplierId, final VehicleId vehicleId) {
        ensureCanBeModified();
        return markAsModifiedWhen(step(stepNumber).reassignCarrier(supplierId, vehicleId));
    }

    private DeliveryStep step(final int stepNumber) {
        return deliverySteps.stream()
                .filter(deliveryStep -> deliveryStep.deliveryStep() == stepNumber)
                .findFirst()
                .orElseThrow(() -> new DeliveryStepNotFoundException("Delivery step " + stepNumber
                        + " was not found in delivery " + deliveryId.getId()));
    }

    private boolean markAsModifiedWhen(final boolean changed) {
        if (changed) {
            markAsModified();
        }
        return changed;
    }

    private void markAsModified() {
        ensureCanBeModified();
        this.status = DeliveryLifecycleStatus.MODIFIED;
    }

    public static Delivery forPallet(final DeliveryTarget target, final UserId userId) {
        if (target.type() != DeliveryTargetType.PALLET) {
            throw new DeliveryModificationException("Pallet delivery requires pallet target");
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
        this.replacedByDeliveryId = palletDeliveryId;
        markAsCanceled();
        return true;
    }

    public void markAsCanceled() {
        if (status == DeliveryLifecycleStatus.COMPLETED) {
            throw new DeliveryModificationException("Completed delivery cannot be canceled");
        }
        this.status = DeliveryLifecycleStatus.CANCELED;
    }

    private void markAsCompleted() {
        if (status == DeliveryLifecycleStatus.CANCELED) {
            throw new DeliveryModificationException("Canceled delivery cannot be completed");
        }
        this.status = DeliveryLifecycleStatus.COMPLETED;
        this.deliveryStatus = DeliveryStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
    }

    public void recordAttempt(final DeliveryStatus attemptStatus, final UserId userId, final SupplierId supplierId,
                              final DepartmentId departmentId, final VehicleId vehicleId, final String comment,
                              final String token) {
        addStep(DeliveryStep.attempt(deliveryId, attemptStatus, userId, supplierId, departmentId, vehicleId, comment,
                token, deliverySteps.size()));
    }

    public void complete(final SupplierId supplierId, final UserId userId, final DepartmentId departmentId) {
        addStep(DeliveryStep.complete(deliveryId, supplierId, userId, departmentId, deliverySteps.size()));
        markAsCompleted();
    }

    public DeliverySnapshot snapshot() {
        return new DeliverySnapshot(deliveryId, target, shipmentId, type, createdAt, supplierId, vehicleId,
                deliveryStatus, status, originDepartmentId, destinationDepartmentId, pickupMethod, method,
                pickupPointId, deliveryPickupPointId, signatureRequired, instructions, plannedAt, deliveredAt, token,
                deliverySteps.stream().map(DeliveryStep::snapshot).toList());
    }

    private void addStep(final DeliveryStep deliveryStep) {
        ensureCanBeModified();
        deliverySteps.add(deliveryStep);
    }

    public void ensureRecipientSignature(final boolean recipientSigned) {
        if (Boolean.TRUE.equals(signatureRequired) && !recipientSigned) {
            throw new DeliveryModificationException("Recipient signature is required for this delivery");
        }
    }

    private void ensureCanBeModified() {
        if (status == DeliveryLifecycleStatus.CANCELED || status == DeliveryLifecycleStatus.COMPLETED) {
            throw new DeliveryModificationException("Delivery cannot be modified when its status is " + status);
        }
    }
}
