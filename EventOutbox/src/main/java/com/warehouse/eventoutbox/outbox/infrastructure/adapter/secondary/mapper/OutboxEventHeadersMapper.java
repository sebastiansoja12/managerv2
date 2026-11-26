package com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.mapper;

import java.util.LinkedHashMap;
import java.util.Map;

import com.warehouse.eventoutbox.integration.model.EventMetadata;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.kafka.KafkaEventHeaders;

public class OutboxEventHeadersMapper {

    private final boolean legacyEnvelope;

    public OutboxEventHeadersMapper(final boolean legacyEnvelope) {
        this.legacyEnvelope = legacyEnvelope;
    }

    public Map<String, String> map(final EventMetadata metadata, final Class<? extends IntegrationEvent> eventClass) {
        final Map<String, String> headers = new LinkedHashMap<>();
        if (this.legacyEnvelope) {
            headers.put(KafkaEventHeaders.TYPE_ID, eventClass.getName());
            headers.put(KafkaEventHeaders.EVENT_ID, metadata.eventId().value().toString());
            headers.put(KafkaEventHeaders.EVENT_TYPE, metadata.eventType());
            headers.put(KafkaEventHeaders.OCCURRED_AT, metadata.occurredAt().toString());
        }
        headers.put(KafkaEventHeaders.CE_SPEC_VERSION, KafkaEventHeaders.CLOUD_EVENTS_SPEC_VERSION);
        headers.put(KafkaEventHeaders.CE_ID, metadata.eventId().value().toString());
        headers.put(KafkaEventHeaders.CE_TYPE, metadata.eventType());
        headers.put(KafkaEventHeaders.CE_SOURCE, metadata.source());
        headers.put(KafkaEventHeaders.CE_TIME, metadata.occurredAt().toString());
        headers.put(KafkaEventHeaders.EVENT_VERSION, String.valueOf(metadata.eventVersion()));
        if (metadata.subject() != null) {
            headers.put(KafkaEventHeaders.CE_SUBJECT, metadata.subject());
        }
        headers.putAll(metadata.attributes());
        return headers;
    }
}
