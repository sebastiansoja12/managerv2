package com.warehouse.returning.domain.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component("returning.domainRegistry")
public final class DomainRegistry implements ApplicationEventPublisherAware {

    private static final Logger log = LoggerFactory.getLogger(DomainRegistry.class);

    private static volatile ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        if (DomainRegistry.eventPublisher == null) {
            synchronized (DomainRegistry.class) {
                if (DomainRegistry.eventPublisher == null) {
                    DomainRegistry.eventPublisher = applicationEventPublisher;
                    log.info("ApplicationEventPublisher initialized in DomainRegistry");
                }
            }
        }
    }

    public static <T> void publish(final T event) {
        final ApplicationEventPublisher publisher = DomainRegistry.eventPublisher;
        if (publisher == null) {
            throw new IllegalStateException("ApplicationEventPublisher not initialized yet");
        }

        log.info("Publishing event of type: {}", event.getClass().getSimpleName());
        publisher.publishEvent(event);
    }
}
