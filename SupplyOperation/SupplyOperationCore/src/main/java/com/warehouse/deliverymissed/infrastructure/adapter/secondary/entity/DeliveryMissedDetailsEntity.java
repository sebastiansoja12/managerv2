package com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity;

import java.math.BigDecimal;
import java.time.Instant;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity.id.DeliveryMissedDetailId;

import jakarta.persistence.*;

@Entity
@Table(name = "delivery_missed_details")
public class DeliveryMissedDetailsEntity {

    @Column(name = "delivery_missed_detail_id")
    @AttributeOverride(name = "value", column = @Column(name = "delivery_missed_detail_id"))
	@EmbeddedId
	private DeliveryMissedDetailId deliveryMissedDetailId;

	@Column(name = "planned_delivery_date", nullable = false)
	private Instant plannedDeliveryDate;

	@Column(name = "delivery_date")
	private Instant deliveryDate;

	@Column(name = "created", nullable = false, updatable = false)
	private Instant created;

	@Column(name = "new_proposed_date")
	private Instant newProposedDate;

	@Column(name = "address_changed", nullable = false)
	private Boolean addressChanged;

	@Column(name = "delivery_attempt_number", nullable = false)
	private Integer deliveryAttemptNumber;

	@Column(name = "penalty_fee")
	private BigDecimal penaltyFee;

	@Column(name = "suggested_action", length = 500)
	private String suggestedAction;

	@Column(name = "incident_report", length = 2000)
	private String incidentReport;

    @Column(name = "shipment_id")
	@Embedded
    @AttributeOverride(name = "value", column = @Column(name = "shipment_id"))
    private ShipmentId shipmentId;

    public DeliveryMissedDetailsEntity() {
    }

    public DeliveryMissedDetailsEntity(final DeliveryMissedDetailId deliveryMissedDetailId,
                                 final Instant plannedDeliveryDate,
                                 final Instant deliveryDate,
                                 final Instant created,
                                 final Instant newProposedDate,
                                 final Boolean addressChanged,
                                 final Integer deliveryAttemptNumber,
                                 final BigDecimal penaltyFee,
                                 final String suggestedAction,
                                 final String incidentReport,
                                 final ShipmentId shipmentId) {
        this.deliveryMissedDetailId = deliveryMissedDetailId;
        this.plannedDeliveryDate = plannedDeliveryDate;
        this.deliveryDate = deliveryDate;
        this.created = created;
        this.newProposedDate = newProposedDate;
        this.addressChanged = addressChanged;
        this.deliveryAttemptNumber = deliveryAttemptNumber;
        this.penaltyFee = penaltyFee;
        this.suggestedAction = suggestedAction;
        this.incidentReport = incidentReport;
        this.shipmentId = shipmentId;
    }

    public DeliveryMissedDetailId getDeliveryMissedDetailId() {
        return deliveryMissedDetailId;
    }

    public void setDeliveryMissedDetailId(final DeliveryMissedDetailId deliveryMissedDetailId) {
        this.deliveryMissedDetailId = deliveryMissedDetailId;
    }

    public Instant getPlannedDeliveryDate() {
        return plannedDeliveryDate;
    }

    public void setPlannedDeliveryDate(final Instant plannedDeliveryDate) {
        this.plannedDeliveryDate = plannedDeliveryDate;
    }

    public Instant getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(final Instant deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(final Instant created) {
        this.created = created;
    }

    public Instant getNewProposedDate() {
        return newProposedDate;
    }

    public void setNewProposedDate(final Instant newProposedDate) {
        this.newProposedDate = newProposedDate;
    }

    public Boolean getAddressChanged() {
        return addressChanged;
    }

    public void setAddressChanged(final Boolean addressChanged) {
        this.addressChanged = addressChanged;
    }

    public Integer getDeliveryAttemptNumber() {
        return deliveryAttemptNumber;
    }

    public void setDeliveryAttemptNumber(final Integer deliveryAttemptNumber) {
        this.deliveryAttemptNumber = deliveryAttemptNumber;
    }

    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(final BigDecimal penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    public String getSuggestedAction() {
        return suggestedAction;
    }

    public void setSuggestedAction(final String suggestedAction) {
        this.suggestedAction = suggestedAction;
    }

    public String getIncidentReport() {
        return incidentReport;
    }

    public void setIncidentReport(final String incidentReport) {
        this.incidentReport = incidentReport;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }
}
