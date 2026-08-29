package com.warehouse.shipment.application.listener;

import com.warehouse.commonassets.event.domain.port.IntegrationEventPublisher;
import com.warehouse.shipment.application.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.application.event.ShipmentDestinationChangedIntegrationEvent;
import com.warehouse.shipment.application.event.ShipmentReturnCanceledIntegrationEvent;
import com.warehouse.shipment.application.event.ShipmentReturnCreatedIntegrationEvent;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.application.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.event.*;
import com.warehouse.shipment.domain.exception.DestinationDepartmentDeterminationException;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.domain.vo.VoronoiResponse;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ShipmentEventListener {

    private final ShipmentPort shipmentPort;

    private final PathFinderServicePort pathFinderServicePort;
    private final IntegrationEventPublisher integrationEventPublisher;

    public ShipmentEventListener(final ShipmentPort shipmentPort,
                                 final PathFinderServicePort pathFinderServicePort,
                                 final IntegrationEventPublisher integrationEventPublisher) {
        this.shipmentPort = shipmentPort;
        this.pathFinderServicePort = pathFinderServicePort;
        this.integrationEventPublisher = integrationEventPublisher;
    }

    @EventListener
    public void handle(final ShipmentCreatedEvent event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();
        final ShipmentCreatedIntegrationEvent integrationEvent = new ShipmentCreatedIntegrationEvent(
                com.warehouse.shipment.application.event.snapshot.ShipmentSnapshot.from(snapshot)
        );
        this.integrationEventPublisher.publish(integrationEvent);
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
        final ShipmentSnapshot snapshot = event.getSnapshot();
        final ShipmentDestinationChangedIntegrationEvent integrationEvent = new ShipmentDestinationChangedIntegrationEvent(
                com.warehouse.shipment.application.event.snapshot.ShipmentSnapshot.from(snapshot)
        );
        this.integrationEventPublisher.publish(integrationEvent);
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
        this.integrationEventPublisher.publish(new ShipmentReturnCanceledIntegrationEvent(event.getSnapshot().shipmentId()));
    }

    @EventListener
    public void handle(final ShipmentReturnCreated event) {
        final ShipmentReturnCreatedIntegrationEvent integrationEvent = new ShipmentReturnCreatedIntegrationEvent(
                com.warehouse.shipment.application.event.snapshot.ShipmentSnapshot.from(event.getSnapshot()),
                event.getTimestamp(),
                event.getReasonCode().name(),
                event.getReason(),
                event.getDepartmentCode()
        );
        this.integrationEventPublisher.publish(integrationEvent);
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
