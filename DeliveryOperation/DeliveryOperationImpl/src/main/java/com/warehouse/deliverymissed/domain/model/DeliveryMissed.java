package com.warehouse.deliverymissed.domain.model;

import java.time.Instant;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliverymissed.domain.vo.DeliveryAttemptNumber;
import com.warehouse.deliverymissed.domain.vo.IncidentReport;
import com.warehouse.deliverymissed.domain.vo.PenaltyFee;
import com.warehouse.deliverymissed.domain.vo.SuggestedAction;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity.id.DeliveryMissedDetailId;

import lombok.Builder;

@Builder
public class DeliveryMissed {
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

	public DeliveryMissed(final DeliveryMissedDetailId deliveryMissedDetailId, final Instant plannedDeliveryDate,
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

    public SuggestedAction getSuggestedAction() {
        return suggestedAction;
    }

    public IncidentReport getIncidentReport() {
        return incidentReport;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }
}
