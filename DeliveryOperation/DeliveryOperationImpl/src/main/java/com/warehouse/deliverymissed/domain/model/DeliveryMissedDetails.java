package com.warehouse.deliverymissed.domain.model;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliverymissed.domain.vo.*;
import lombok.Builder;

import java.time.Instant;

@Builder
public class DeliveryMissedDetails {
    private DeliveryMissedDetailId deliveryMissedDetailId;
    private Instant plannedDeliveryDate;
    private Instant deliveryDate;
    private Instant created;
    private Instant newProposedDate;
    private Boolean addressChanged;
    private DeliveryAttemptNumber deliveryAttemptNumber;
    private PenaltyFee penaltyFee;
    private SuggestedAction suggestedAction;
    private IncidentReport incidentReport;
    private ShipmentId shipmentId;

	public DeliveryMissedDetails(final DeliveryMissedDetailId deliveryMissedDetailId, final Instant plannedDeliveryDate,
			final Instant deliveryDate, final Instant created, final Instant newProposedDate,
			final Boolean addressChanged, final DeliveryAttemptNumber deliveryAttemptNumber,
			final PenaltyFee penaltyFee, final SuggestedAction suggestedAction, final IncidentReport incidentReport,
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

    public DeliveryAttemptNumber getDeliveryAttemptNumber() {
        return deliveryAttemptNumber;
    }

    public void setDeliveryAttemptNumber(final DeliveryAttemptNumber deliveryAttemptNumber) {
        this.deliveryAttemptNumber = deliveryAttemptNumber;
    }

    public PenaltyFee getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(final PenaltyFee penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    public SuggestedAction getSuggestedAction() {
        return suggestedAction;
    }

    public void setSuggestedAction(final SuggestedAction suggestedAction) {
        this.suggestedAction = suggestedAction;
    }

    public IncidentReport getIncidentReport() {
        return incidentReport;
    }

    public void setIncidentReport(final IncidentReport incidentReport) {
        this.incidentReport = incidentReport;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }
}
