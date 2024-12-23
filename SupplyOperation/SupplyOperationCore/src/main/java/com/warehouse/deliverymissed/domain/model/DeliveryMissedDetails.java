package com.warehouse.deliverymissed.domain.model;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliverymissed.domain.vo.DeliveryAttemptNumber;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedDetailId;
import com.warehouse.deliverymissed.domain.vo.PenaltyFee;
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
    private String suggestedAction;
    private String incidentReport;
    private ShipmentId shipmentId;

    public DeliveryMissedDetails(final DeliveryMissedDetailId deliveryMissedDetailId,
                                 final Instant plannedDeliveryDate,
                                 final Instant deliveryDate,
                                 final Instant created,
                                 final Instant newProposedDate,
                                 final Boolean addressChanged,
                                 final DeliveryAttemptNumber deliveryAttemptNumber,
                                 final PenaltyFee penaltyFee,
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

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public Instant getPlannedDeliveryDate() {
        return plannedDeliveryDate;
    }

    public Instant getDeliveryDate() {
        return deliveryDate;
    }

    public Instant getCreated() {
        return created;
    }

    public Instant getNewProposedDate() {
        return newProposedDate;
    }

    public Boolean getAddressChanged() {
        return addressChanged;
    }

    public DeliveryAttemptNumber getDeliveryAttemptNumber() {
        return deliveryAttemptNumber;
    }

    public PenaltyFee getPenaltyFee() {
        return penaltyFee;
    }

    public String getSuggestedAction() {
        return suggestedAction;
    }

    public String getIncidentReport() {
        return incidentReport;
    }

    public void setDeliveryMissedDetailId(final DeliveryMissedDetailId deliveryMissedDetailId) {
        this.deliveryMissedDetailId = deliveryMissedDetailId;
    }

    public void setPlannedDeliveryDate(final Instant plannedDeliveryDate) {
        this.plannedDeliveryDate = plannedDeliveryDate;
    }

    public void setDeliveryDate(final Instant deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setCreated(final Instant created) {
        this.created = created;
    }

    public void setNewProposedDate(final Instant newProposedDate) {
        this.newProposedDate = newProposedDate;
    }

    public void setAddressChanged(final Boolean addressChanged) {
        this.addressChanged = addressChanged;
    }

    public void setDeliveryAttemptNumber(final DeliveryAttemptNumber deliveryAttemptNumber) {
        this.deliveryAttemptNumber = deliveryAttemptNumber;
    }

    public void setPenaltyFee(final PenaltyFee penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    public void setSuggestedAction(final String suggestedAction) {
        this.suggestedAction = suggestedAction;
    }

    public void setIncidentReport(final String incidentReport) {
        this.incidentReport = incidentReport;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }
}
