package com.warehouse.commonassets.event.infrastructure.adapter.secondary;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.event.domain.model.DomainEvent;

@Component
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public SpringDomainEventPublisher(final ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publish(final DomainEvent event) {
        this.eventPublisher.publishEvent(event);
    }
}
