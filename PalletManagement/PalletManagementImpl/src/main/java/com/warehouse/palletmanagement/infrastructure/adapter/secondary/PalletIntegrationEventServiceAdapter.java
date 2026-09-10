package com.warehouse.palletmanagement.infrastructure.adapter.secondary;

import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.palletmanagement.application.port.secondary.PalletIntegrationEventServicePort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = {"manager.kafka.integration-events.enabled", "manager.kafka.outbox.enabled"},
        havingValue = "true")
public class PalletIntegrationEventServiceAdapter implements PalletIntegrationEventServicePort {

    private final IntegrationEventPublisher integrationEventPublisher;

    public PalletIntegrationEventServiceAdapter(final IntegrationEventPublisher integrationEventPublisher) {
        this.integrationEventPublisher = integrationEventPublisher;
    }

    @Override
    public void publishEvent(final IntegrationEvent event) {
        this.integrationEventPublisher.publish(event);
    }
}
