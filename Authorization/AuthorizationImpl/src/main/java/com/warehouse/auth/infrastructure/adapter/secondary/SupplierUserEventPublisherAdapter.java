package com.warehouse.auth.infrastructure.adapter.secondary;

import org.springframework.context.ApplicationEventPublisher;

import com.warehouse.auth.SupplierUserEventPublisher;
import com.warehouse.auth.event.SupplierCreatedEvent;

public class SupplierUserEventPublisherAdapter implements SupplierUserEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SupplierUserEventPublisherAdapter(final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void sendEvent(final SupplierCreatedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
