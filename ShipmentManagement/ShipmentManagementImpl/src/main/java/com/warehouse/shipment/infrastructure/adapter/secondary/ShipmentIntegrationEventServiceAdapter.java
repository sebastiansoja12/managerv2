package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.shipment.application.port.secondary.ShipmentIntegrationEventServicePort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = {"manager.kafka.integration-events.enabled", "manager.kafka.outbox.enabled"},
        havingValue = "true")
public class ShipmentIntegrationEventServiceAdapter implements ShipmentIntegrationEventServicePort {

    private final IntegrationEventPublisher integrationEventPublisher;

    public ShipmentIntegrationEventServiceAdapter(final IntegrationEventPublisher integrationEventPublisher) {
        this.integrationEventPublisher = integrationEventPublisher;
    }

    @Override
    public void publishEvent(final IntegrationEvent event) {
        integrationEventPublisher.publish(event);
    }
}
