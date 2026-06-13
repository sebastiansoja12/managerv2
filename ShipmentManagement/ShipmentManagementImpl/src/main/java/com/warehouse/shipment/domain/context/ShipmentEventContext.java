package com.warehouse.shipment.domain.context;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component("shipment.eventContext")
public final class ShipmentEventContext implements ApplicationEventPublisherAware {

    private static volatile ApplicationEventPublisher eventPublisher;

    public static ApplicationEventPublisher eventPublisher() {
        final ApplicationEventPublisher publisher = eventPublisher;
        if (publisher == null) {
            throw new IllegalStateException("Shipment ApplicationEventPublisher has not been initialized yet");
        }
        return publisher;
    }

    @Override
    public void setApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        ShipmentEventContext.eventPublisher = applicationEventPublisher;
    }
}
