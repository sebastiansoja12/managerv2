package com.warehouse.eventoutbox.configuration;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.eventoutbox.consumption.IntegrationEventListenerRegistrar;
import com.warehouse.eventoutbox.integration.registry.IntegrationEventRegistry;

@Configuration
public class IntegrationEventListenerConfiguration {

    @Bean
    public static IntegrationEventListenerRegistrar integrationEventListenerRegistrar(
            final ObjectProvider<IntegrationEventRegistry> integrationEventRegistry) {
        return new IntegrationEventListenerRegistrar(integrationEventRegistry);
    }
}
