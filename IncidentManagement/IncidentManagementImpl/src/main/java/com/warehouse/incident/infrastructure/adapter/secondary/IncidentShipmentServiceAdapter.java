package com.warehouse.incident.infrastructure.adapter.secondary;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.incident.application.port.secondary.IncidentShipmentServicePort;
import com.warehouse.incident.domain.vo.IncidentEntry;
import com.warehouse.incident.domain.model.Incident;
import com.warehouse.shipment.api.event.IncidentCommented;
import com.warehouse.shipment.api.event.IncidentReopened;
import com.warehouse.shipment.api.event.IncidentReported;
import com.warehouse.shipment.api.event.IncidentResolved;
import com.warehouse.shipment.api.event.snapshot.ShipmentIncidentAction;
import com.warehouse.shipment.api.event.snapshot.ShipmentIncidentData;
import com.warehouse.shipment.api.event.snapshot.ShipmentRepackagingData;
import com.warehouse.incident.domain.vo.IncidentRepackaging;
import com.warehouse.incident.domain.vo.IncidentShipment;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentDetailsDto;

public class IncidentShipmentServiceAdapter implements IncidentShipmentServicePort {

    private final ShipmentApiService shipmentApiService;

    private final IntegrationEventPublisher integrationEventPublisher;

    public IncidentShipmentServiceAdapter(final ShipmentApiService shipmentApiService,
                                          final IntegrationEventPublisher integrationEventPublisher) {
        this.shipmentApiService = shipmentApiService;
        this.integrationEventPublisher = integrationEventPublisher;
    }

    @Override
    public IncidentShipment getShipment(final ShipmentId shipmentId) {
        final ShipmentDetailsDto shipment = this.shipmentApiService.getShipment(shipmentId);
        return new IncidentShipment(shipment.shipmentId(),
                shipment.trackingNumber() == null ? null : new TrackingNumber(shipment.trackingNumber()),
                ShipmentStatus.LOST.equals(shipment.status()));
    }

    @Override
    public void notifyReported(final Incident incident) {
        this.integrationEventPublisher.publish(new IncidentReported(
                data(incident, incident.entries().getFirst(), incident.category().name())));
    }

    @Override
    public void notifyCommented(final Incident incident) {
        this.integrationEventPublisher.publish(new IncidentCommented(
                data(incident, incident.entries().getLast(), null)));
    }

    @Override
    public void notifyResolved(final Incident incident, final IncidentRepackaging repackaging) {
        final ShipmentIncidentAction action = switch (incident.resolution()) {
            case WRITTEN_OFF, DESTROYED_DISPOSED, STOLEN -> ShipmentIncidentAction.MARK_LOST;
            case RETURN_CREATED, REFUSED_BY_RECIPIENT -> ShipmentIncidentAction.RETURN_TO_SENDER;
            case CANCELED -> ShipmentIncidentAction.CANCEL;
            case DELIVERED_WITHOUT_SCAN -> ShipmentIncidentAction.MARK_DELIVERED;
            case REPACKED -> repackaging == null ? null : ShipmentIncidentAction.REPACK;
            case EXPLAINED, DATA_CORRECTED, DUPLICATE, INVALID_REPORT, COSMETIC_DAMAGE_ACCEPTED,
                 DAMAGE_CLAIM_FILED, FOUND, MISSORTED_REDIRECTED, ADDRESS_CORRECTED, ADDRESS_OBTAINED,
                 REDIRECTED_TO_PICKUP_POINT, LABEL_REPRINTED, MANUALLY_LABELED, WEIGHT_CORRECTED,
                 WEIGHT_CORRECTED_SURCHARGE, DOCUMENTS_RECEIVED, DOCUMENTS_REGENERATED, REROUTED -> null;
        };
        this.integrationEventPublisher.publish(new IncidentResolved(
                data(incident, incident.entries().getLast(), incident.resolution().name()), action,
                ShipmentIncidentAction.REPACK.equals(action) ? new ShipmentRepackagingData(
                        repackaging.packagingType(), repackaging.weightInKilograms(),
                        repackaging.lengthInCentimeters(), repackaging.widthInCentimeters(),
                        repackaging.heightInCentimeters()) : null));
    }

    @Override
    public void notifyReopened(final Incident incident) {
        this.integrationEventPublisher.publish(new IncidentReopened(
                data(incident, incident.entries().getLast(), null)));
    }

    private ShipmentIncidentData data(final Incident incident, final IncidentEntry entry, final String reason) {
        return new ShipmentIncidentData(incident.shipment().shipmentId(), incident.incidentId(),
                entry.actor().userId(), reason, entry.text(), entry.occurredAt());
    }
}
