package com.warehouse.eventoutbox.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.warehouse.eventoutbox.outbox.configuration.OutboxConfiguration;
import com.warehouse.eventoutbox.outbox.configuration.OutboxTracingConfiguration;

/**
 * Single entry point for services that do not component-scan {@code com.warehouse.eventoutbox}: registry of
 * integration event types, typed {@code @IntegrationEventListener} consumers, domain to integration event
 * translation, the transactional outbox ({@code manager.events.outbox.*}) and, with Micrometer Tracing on the
 * classpath, trace propagation through it.
 *
 * <p>The service still has to scan {@code com.warehouse.eventoutbox.outbox} for JPA entities and repositories and
 * create the {@code integration_event_outbox} table.
 */
@Configuration
@Import({IntegrationEventRegistryConfiguration.class, IntegrationEventListenerConfiguration.class,
        IntegrationEventTranslationConfiguration.class, OutboxConfiguration.class,
        OutboxTracingConfiguration.class})
public class EventOutboxConfiguration {
}
