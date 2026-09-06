package com.warehouse.logistics.domain.model;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.logistics.domain.enumeration.DeliveryStepOutcome;

import java.time.LocalDateTime;

public class DeliveryStep {

	private DeliveryStepId id;
	private DeliveryId deliveryId;
	private LocalDateTime attemptedAt;
	private DeliveryStepOutcome outcome;
	private DeliveryStatus deliveryStatus;
	private SignatureId signatureId;
	private UserId userId;
	private SupplierId supplierId;
	private DepartmentId departmentId;
	private VehicleId vehicleId;
	private String comment;
	private String failureReason;
	private String token;
	private int deliveryStep;

	public DeliveryStep(final DeliveryStepId id, final DeliveryId deliveryId,
						final DeliveryStepOutcome outcome, final DeliveryStatus deliveryStatus,
			final SignatureId signatureId, final UserId userId, final SupplierId supplierId,
			final DepartmentId departmentId, final VehicleId vehicleId,
			final String comment, final String failureReason, final String token, final int deliveryStep) {

		this.id = id;
		this.deliveryId = deliveryId;
		this.attemptedAt = LocalDateTime.now();
		this.outcome = outcome;
		this.deliveryStatus = deliveryStatus;
		this.signatureId = signatureId;
		this.userId = userId;
		this.supplierId = supplierId;
		this.departmentId = departmentId;
		this.vehicleId = vehicleId;
		this.comment = comment;
		this.failureReason = failureReason;
		this.token = token;
		this.deliveryStep = deliveryStep;
	}

	public DeliveryStep(final DeliveryStepId id, final DeliveryId deliveryId,
						final LocalDateTime attemptedAt,
						final DeliveryStepOutcome outcome, final DeliveryStatus deliveryStatus,
						final SignatureId signatureId, final UserId userId, final SupplierId supplierId,
						final DepartmentId departmentId, final VehicleId vehicleId,
						final String comment, final String failureReason, final String token, final int deliverySteps) {

		this.id = id;
		this.deliveryId = deliveryId;
		this.attemptedAt = attemptedAt;
		this.outcome = outcome;
		this.deliveryStatus = deliveryStatus;
		this.signatureId = signatureId;
		this.userId = userId;
		this.supplierId = supplierId;
		this.departmentId = departmentId;
		this.vehicleId = vehicleId;
		this.comment = comment;
		this.failureReason = failureReason;
		this.token = token;
		this.deliveryStep = deliverySteps + 1;
	}

	public static DeliveryStep created(final DeliveryId deliveryId, final LocalDateTime createdAt,
			final SignatureId signatureId, final String comment, final UserId userId) {

		return new DeliveryStep(DeliveryStepId.generate(), deliveryId, createdAt, DeliveryStepOutcome.IN_PROGRESS,
				DeliveryStatus.DEPOT, signatureId, userId, null, null, null, comment, null, null, 0);
	}

	public static DeliveryStep attempt(final DeliveryId deliveryId, final DeliveryStatus deliveryStatus, final UserId userId,
			final SupplierId supplierId, final DepartmentId departmentId, final VehicleId vehicleId,
			final String comment, final String token, final int deliverySteps) {

		final DeliveryStepOutcome outcome = outcome(deliveryStatus);
		final String failureReason = outcome == DeliveryStepOutcome.FAILED ? comment : null;

		return new DeliveryStep(DeliveryStepId.generate(), deliveryId, LocalDateTime.now(), outcome, deliveryStatus,
				null, userId, supplierId, departmentId, vehicleId, comment, failureReason, token, deliverySteps);
	}

	private static DeliveryStepOutcome outcome(final DeliveryStatus deliveryStatus) {

		if (deliveryStatus == DeliveryStatus.DELIVERED) {
			return DeliveryStepOutcome.SUCCEEDED;
		}

		if (deliveryStatus == DeliveryStatus.REJECTED || deliveryStatus == DeliveryStatus.UNAVAILABLE
				|| deliveryStatus == DeliveryStatus.LOST) {
			return DeliveryStepOutcome.FAILED;
		}

		return DeliveryStepOutcome.IN_PROGRESS;
	}

	public static DeliveryStep complete(final DeliveryId deliveryId, final SupplierId supplierId, final UserId userId,
			final DepartmentId departmentId, final int deliverySteps) {
		final DeliveryStepOutcome outcome = outcome(DeliveryStatus.DELIVERED);
		return new DeliveryStep(DeliveryStepId.generate(), deliveryId, LocalDateTime.now(), outcome,
				DeliveryStatus.DELIVERED, null, userId,
				supplierId, departmentId, null, null, null, null,
				deliverySteps);
	}

	public DeliveryStepId id() {
		return id;
	}

	public DeliveryId deliveryId() {
		return deliveryId;
	}

	public LocalDateTime attemptedAt() {
		return attemptedAt;
	}

	public DeliveryStepOutcome outcome() {
		return outcome;
	}

	public DeliveryStatus deliveryStatus() {
		return deliveryStatus;
	}

	public SignatureId signatureId() {
		return signatureId;
	}

	public UserId userId() {
		return userId;
	}

	public SupplierId supplierId() {
		return supplierId;
	}

	public DepartmentId departmentId() {
		return departmentId;
	}

	public VehicleId vehicleId() {
		return vehicleId;
	}

	public String comment() {
		return comment;
	}

	public String failureReason() {
		return failureReason;
	}

	public String token() {
		return token;
	}

	public int deliveryStep() {
		return deliveryStep;
	}
}
