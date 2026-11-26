package com.warehouse.eventoutbox.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import com.warehouse.eventoutbox.integration.registry.IntegrationEventRegistry;
import com.warehouse.eventoutbox.integration.registry.IntegrationEventTypeScanner;

@Configuration
public class IntegrationEventRegistryConfiguration {

    private static final String INTEGRATION_EVENTS_BASE_PACKAGE = "com.warehouse";

    @Bean
    public IntegrationEventRegistry integrationEventRegistry(final ResourceLoader resourceLoader) {
        return new IntegrationEventTypeScanner(resourceLoader.getClassLoader()).scan(INTEGRATION_EVENTS_BASE_PACKAGE);
    }
}
