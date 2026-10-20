package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary;

import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.logisticsdelivery.api.event.DeliveryChangedIntegrationEvent;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryIntegrationEventServicePort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = {"manager.kafka.integration-events.enabled", "manager.kafka.outbox.enabled"},
        havingValue = "true")
public class DeliveryIntegrationEventServiceAdapter implements DeliveryIntegrationEventServicePort {

    private final IntegrationEventPublisher integrationEventPublisher;

    public DeliveryIntegrationEventServiceAdapter(final IntegrationEventPublisher integrationEventPublisher) {
        this.integrationEventPublisher = integrationEventPublisher;
    }

    @Override
    public void publishEvent(final DeliveryChangedIntegrationEvent event) {
        this.integrationEventPublisher.publish(event);
    }
}
