package com.warehouse.department.domain.registry;

import com.warehouse.department.domain.service.AuthenticationService;
import com.warehouse.department.domain.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component("department.domainRegistry")
public final class DomainRegistry implements ApplicationEventPublisherAware, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(DomainRegistry.class);

    private static volatile ApplicationEventPublisher eventPublisher;

    private static ApplicationContext context;

    public static AuthenticationService authenticationService() {
        return context.getBean(AuthenticationService.class);
    }

    public static DepartmentService departmentService() {
        return context.getBean(DepartmentService.class);
    }

    public static synchronized ApplicationEventPublisher eventPublisher() {
        return eventPublisher;
    }

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

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
