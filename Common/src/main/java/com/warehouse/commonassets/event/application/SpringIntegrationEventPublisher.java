package com.warehouse.commonassets.event.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.warehouse.commonassets.event.domain.model.IntegrationEvent;
import com.warehouse.commonassets.event.domain.port.IntegrationEventPublisher;

@Component
public class SpringIntegrationEventPublisher implements IntegrationEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public SpringIntegrationEventPublisher(final ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publish(final IntegrationEvent event) {
        this.eventPublisher.publishEvent(event);
    }
}
