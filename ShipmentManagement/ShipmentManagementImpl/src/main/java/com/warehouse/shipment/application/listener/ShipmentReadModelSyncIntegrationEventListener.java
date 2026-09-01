package com.warehouse.shipment.application.listener;

import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.shipment.application.event.ShipmentReadModelChanged;
import com.warehouse.shipment.application.event.snapshot.ShipmentReadModelData;
import com.warehouse.shipment.domain.event.ShipmentChanged;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = {"manager.kafka.shipment-read-model-sync.enabled", "manager.kafka.outbox.enabled"},
        havingValue = "true")
public class ShipmentReadModelSyncIntegrationEventListener {

    private final IntegrationEventPublisher integrationEventPublisher;

    public ShipmentReadModelSyncIntegrationEventListener(
            final IntegrationEventPublisher integrationEventPublisher) {
        this.integrationEventPublisher = integrationEventPublisher;
    }

    @EventListener
    public void handle(final ShipmentChanged event) {
        this.integrationEventPublisher.publish(
                new ShipmentReadModelChanged(
                        new ShipmentReadModelData(event.getSnapshot().shipmentId()),
                        event.getTimestamp()));
    }
}
