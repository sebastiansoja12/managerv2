package com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.mapper;

import com.warehouse.eventoutbox.integration.context.IntegrationEventContext;
import com.warehouse.eventoutbox.integration.model.EventMetadata;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

public class OutboxEventPayloadMapper {

    private final ObjectMapper objectMapper;
    private final boolean legacyEnvelope;
    private final IntegrationEventContext context;

    public OutboxEventPayloadMapper(final ObjectMapper objectMapper,
                                    final boolean legacyEnvelope,
                                    final IntegrationEventContext context) {
        this.objectMapper = objectMapper;
        this.legacyEnvelope = legacyEnvelope;
        this.context = context;
    }

    public String map(final IntegrationEvent event, final EventMetadata metadata) {
        try {
            if (!this.legacyEnvelope) {
                return this.objectMapper.writeValueAsString(event);
            }
            final ObjectNode message = this.objectMapper.valueToTree(event);
            message.put("eventId", metadata.eventId().value().toString());
            message.put("eventType", metadata.eventType());
            message.put("version", metadata.eventVersion());
            message.put("occurredAt", metadata.occurredAt().toString());
            this.context.enrichLegacyPayload(message, metadata.attributes());
            return this.objectMapper.writeValueAsString(message);
        } catch (final JacksonException exception) {
            throw new IllegalStateException("Cannot serialize integration event: " + event.getClass().getName(),
                    exception);
        }
    }
}
