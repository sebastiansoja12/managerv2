package com.warehouse.dangerousgood.domain.registry;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component("dangerousGood.domainRegistry")
public class DomainRegistry implements ApplicationEventPublisherAware {

    private static ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        DomainRegistry.eventPublisher = applicationEventPublisher;
    }

    public static void publish(final Object event) {
        if (eventPublisher != null) {
            eventPublisher.publishEvent(event);
        }
    }
}
