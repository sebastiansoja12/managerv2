package com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary;

import java.time.Clock;

import com.warehouse.eventoutbox.integration.context.IntegrationEventContext;
import com.warehouse.eventoutbox.integration.model.EventId;
import com.warehouse.eventoutbox.integration.model.EventMetadata;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.integration.registry.IntegrationEventDefinition;

public class EventMetadataFactory {

    private final IntegrationEventContext context;
    private final Clock clock;
    private final String source;

    public EventMetadataFactory(final IntegrationEventContext context,
                                final Clock clock,
                                final String source) {
        this.context = context;
        this.clock = clock;
        this.source = source;
    }

    public EventMetadata create(final IntegrationEventDefinition<?> definition, final IntegrationEvent event) {
        return new EventMetadata(
                EventId.nextId(),
                definition.type(),
                definition.version(),
                this.clock.instant(),
                this.source,
                definition.messageKey(event).orElse(null),
                this.context.attributes(event));
    }
}
