package com.warehouse.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;

@Configuration
public class EventsConfig {

    @Bean(name = AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME)
    public org.springframework.context.event.ApplicationEventMulticaster applicationEventMulticaster() {
        return new TenantAwareEventMulticaster();
    }
}

