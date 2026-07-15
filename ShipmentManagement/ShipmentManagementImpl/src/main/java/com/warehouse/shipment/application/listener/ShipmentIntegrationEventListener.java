package com.warehouse.shipment.application.listener;

import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.shipment.application.event.*;
import com.warehouse.shipment.application.event.snapshot.ShipmentEventData;
import com.warehouse.shipment.domain.event.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = {"manager.kafka.integration-events.enabled", "manager.kafka.outbox.enabled"},
        havingValue = "true")
public class ShipmentIntegrationEventListener {

    private final IntegrationEventPublisher integrationEventPublisher;

    public ShipmentIntegrationEventListener(final IntegrationEventPublisher integrationEventPublisher) {
        this.integrationEventPublisher = integrationEventPublisher;
    }

    @EventListener
    public void handle(final ShipmentCreated event) {
        this.integrationEventPublisher.publish(
                new ShipmentCreatedIntegrationEvent(ShipmentEventData.from(event.getSnapshot())));
    }

    @EventListener
    public void handle(final ShipmentDestinationChanged event) {
        this.integrationEventPublisher.publish(
                new ShipmentDestinationChangedIntegrationEvent(ShipmentEventData.from(event.getSnapshot())));
    }

    @EventListener
    public void handle(final ShipmentReturnCanceled event) {
        this.integrationEventPublisher.publish(
                new ShipmentReturnCanceledIntegrationEvent(ShipmentEventData.from(event.getSnapshot())));
    }

    @EventListener
    public void handle(final ShipmentStatusChanged event) {
        this.integrationEventPublisher.publish(
                new ShipmentStatusChangedIntegrationEvent(ShipmentEventData.from(event.getSnapshot())));
    }

    @EventListener
    public void handle(final ShipmentReturnCreated event) {
        this.integrationEventPublisher.publish(new ShipmentReturnCreatedIntegrationEvent(
                ShipmentEventData.from(event.getSnapshot()),
                event.getTimestamp(),
                event.getReasonCode().name(),
                event.getReason(),
                event.getDepartmentCode()
        ));
    }
}
