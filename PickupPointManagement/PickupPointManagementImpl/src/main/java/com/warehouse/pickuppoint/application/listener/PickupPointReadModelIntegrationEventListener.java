package com.warehouse.pickuppoint.application.listener;

import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.pickuppoint.api.dto.PickupPointIdDto;
import com.warehouse.pickuppoint.api.event.PickupPointReadModelChangedIntegrationEvent;
import com.warehouse.pickuppoint.api.event.snapshot.PickupPointReadModelData;
import com.warehouse.pickuppoint.domain.event.PickupPointChanged;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = {"manager.kafka.pickup-point-read-model-sync.enabled", "manager.kafka.outbox.enabled"},
        havingValue = "true")
public class PickupPointReadModelIntegrationEventListener {

    private final IntegrationEventPublisher integrationEventPublisher;

    public PickupPointReadModelIntegrationEventListener(
            final IntegrationEventPublisher integrationEventPublisher) {
        this.integrationEventPublisher = integrationEventPublisher;
    }

    @EventListener
    public void handle(final PickupPointChanged event) {
        this.integrationEventPublisher.publish(new PickupPointReadModelChangedIntegrationEvent(
                new PickupPointReadModelData(
                        new PickupPointIdDto(event.getSnapshot().pickupPointId().value())),
                event.getTimestamp()));
    }
}
