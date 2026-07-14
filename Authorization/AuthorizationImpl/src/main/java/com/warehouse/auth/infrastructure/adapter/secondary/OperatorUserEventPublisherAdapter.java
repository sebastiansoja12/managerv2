package com.warehouse.auth.infrastructure.adapter.secondary;

import org.springframework.context.ApplicationEventPublisher;

import com.warehouse.auth.OperatorUserEventPublisher;
import com.warehouse.auth.event.OperatorCreatedEvent;

public class OperatorUserEventPublisherAdapter implements OperatorUserEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public OperatorUserEventPublisherAdapter(final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void sendEvent(final OperatorCreatedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
