package com.warehouse.eventoutbox.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.warehouse.eventoutbox.outbox.configuration.OutboxConfiguration;
import com.warehouse.eventoutbox.outbox.configuration.OutboxTracingConfiguration;

@Configuration
@Import({IntegrationEventRegistryConfiguration.class, IntegrationEventListenerConfiguration.class,
        IntegrationEventTranslationConfiguration.class, OutboxConfiguration.class,
        OutboxTracingConfiguration.class})
public class EventOutboxConfiguration {
}
