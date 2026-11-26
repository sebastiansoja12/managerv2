package com.warehouse.eventoutbox.infrastructure.adapter.secondary;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import com.warehouse.eventoutbox.domain.model.DomainEvent;

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
