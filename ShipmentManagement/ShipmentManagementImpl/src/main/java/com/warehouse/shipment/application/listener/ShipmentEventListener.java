package com.warehouse.shipment.application.listener;

import java.util.UUID;

import com.warehouse.commonassets.kafka.application.IntegrationEventOutboxWriter;
import com.warehouse.shipment.application.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.application.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.event.*;
import com.warehouse.shipment.domain.exception.DestinationDepartmentDeterminationException;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ShipmentEventListener {

    private static final String SHIPMENT_CREATED_EVENT_TYPE = "shipment.created";
    private static final int SHIPMENT_CREATED_EVENT_VERSION = 1;

    private final ShipmentPort shipmentPort;

    private final PathFinderServicePort pathFinderServicePort;
    private final ObjectProvider<IntegrationEventOutboxWriter> integrationEventOutboxWriter;
    private final String shipmentEventsTopic;

    public ShipmentEventListener(final ShipmentPort shipmentPort,
                                 final PathFinderServicePort pathFinderServicePort,
                                 final ObjectProvider<IntegrationEventOutboxWriter> integrationEventOutboxWriter,
                                 @Value("${manager.kafka.topics.shipment-events:shipment.events}")
                                 final String shipmentEventsTopic) {
        this.shipmentPort = shipmentPort;
        this.pathFinderServicePort = pathFinderServicePort;
        this.integrationEventOutboxWriter = integrationEventOutboxWriter;
        this.shipmentEventsTopic = shipmentEventsTopic;
    }

    @EventListener
    public void handle(final ShipmentCreatedEvent event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();
        final ShipmentCreatedIntegrationEvent integrationEvent = new ShipmentCreatedIntegrationEvent(
                UUID.randomUUID(),
                SHIPMENT_CREATED_EVENT_TYPE,
                SHIPMENT_CREATED_EVENT_VERSION,
                event.getTimestamp(),
                com.warehouse.shipment.application.event.snapshot.ShipmentSnapshot.from(snapshot)
        );
        final IntegrationEventOutboxWriter outboxWriter = this.integrationEventOutboxWriter.getIfAvailable();
        if (outboxWriter == null) {
            return;
        }
        outboxWriter.write(
                this.shipmentEventsTopic,
                String.valueOf(snapshot.shipmentId().getValue()),
                integrationEvent.eventId(),
                integrationEvent.eventType(),
                integrationEvent.version(),
                integrationEvent.occurredAt(),
                integrationEvent
        );
    }

    @EventListener
    public void handle(final ShipmentCanceled event) {
    }

    @EventListener
    public void handle(final ShipmentCountriesChanged event) {
    }

    @EventListener
    public void handle(final ShipmentCurrencyChanged event) {
    }

    @EventListener
    public void handle(final ShipmentDangerousGoodAdded event) {
    }

    @EventListener
    public void handle(final ShipmentDangerousGoodRemoved event) {
    }

    @EventListener
    public void handle(final ShipmentDangerousGoodUpdated event) {
    }

    @EventListener
    public void handle(final ShipmentDelivered event) {
    }

    @EventListener
    public void handle(final ShipmentDestinationChanged event) {
    }

    @EventListener
    public void handle(final ShipmentRecipientChanged event) {
    }

    @EventListener
    public void handle(final ShipmentRelatedLocked event) {
    }

    @EventListener
    public void handle(final ShipmentRerouted event) {
    }

    @EventListener
    public void handle(final ShipmentReturnCanceled event) {
    }

    @EventListener
    public void handle(final ShipmentReturnCreated event) {
    }

    @EventListener
    public void handle(final ShipmentSenderChanged event) {
    }

    @EventListener
    public void handle(final ShipmentSent event) {
    }

    @EventListener
    public void handle(final ShipmentStatusChangedEvent event) {
    }

    @EventListener
    public void handle(final ShipmentTypeChanged event) {
    }

    @EventListener
    public void handle(final ShipmentUpdated event) {
    }

    @EventListener
    public void handle(final SignatureSigned event) {
    }

    @TransactionalEventListener(fallbackExecution = true)
    public void handle(final ShipmentReturned event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();
        final Result<VoronoiResponse, ErrorCode> destinationResult = this.pathFinderServicePort
                .determineDeliveryDepartment(Address.from(snapshot.sender()));

        if (destinationResult.isFailure()) {
            throw new DestinationDepartmentDeterminationException(destinationResult.getFailure());
        }

        final VoronoiResponse voronoiResponse = destinationResult.getSuccess();
        this.shipmentPort.changeDestination(snapshot.shipmentId(), voronoiResponse.getDepartmentCodeResult());
    }

    @TransactionalEventListener(fallbackExecution = true)
    public void handle(final ShipmentLocked event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();
        this.shipmentPort.lockShipment(snapshot.shipmentId());
    }

    @EventListener
    public void handle(final ShipmentRedirected event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();
        this.shipmentPort.redirectShipmentToSender(snapshot.shipmentId());
    }

}
