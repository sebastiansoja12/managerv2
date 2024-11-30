package com.warehouse.deliverymissed.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import lombok.Builder;

import java.time.Instant;

@Builder
public class DeliveryMissedDetail {
    private final DeliveryMissedDetailId deliveryMissedDetailId;
    private final Instant plannedDeliveryDate;
    private final Instant deliveryDate;
    private final Instant created;
    private final Instant newProposedDate;
    private final Boolean addressChanged;
    private final DeliveryAttemptNumber deliveryAttemptNumber;
    private final PenaltyFee penaltyFee;
    private final String suggestedAction;
    private final String incidentReport;
    private final ShipmentId shipmentId;

    public DeliveryMissedDetail(final DeliveryMissedDetailId deliveryMissedDetailId,
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
}
