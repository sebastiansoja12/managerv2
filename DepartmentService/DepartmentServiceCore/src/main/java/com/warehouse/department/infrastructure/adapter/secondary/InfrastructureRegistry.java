package com.warehouse.department.infrastructure.adapter.secondary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component("department.infrastructureRegistry")
public final class InfrastructureRegistry implements ApplicationEventPublisherAware, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(InfrastructureRegistry.class);

    private static volatile ApplicationEventPublisher eventPublisher;

    private static ApplicationContext context;

    public static synchronized ApplicationEventPublisher eventPublisher() {
        return eventPublisher;
    }

    @Override
    public void setApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        if (InfrastructureRegistry.eventPublisher == null) {
            synchronized (InfrastructureRegistry.class) {
                if (InfrastructureRegistry.eventPublisher == null) {
                    InfrastructureRegistry.eventPublisher = applicationEventPublisher;
                    log.info("ApplicationEventPublisher initialized in DomainRegistry");
                }
            }
        }
    }

    public static <T> void publish(final T event) {
        final ApplicationEventPublisher publisher = InfrastructureRegistry.eventPublisher;
        if (publisher == null) {
            throw new IllegalStateException("ApplicationEventPublisher not initialized yet");
        }

        log.info("Publishing event of type: {}", event.getClass().getSimpleName());
        publisher.publishEvent(event);
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
