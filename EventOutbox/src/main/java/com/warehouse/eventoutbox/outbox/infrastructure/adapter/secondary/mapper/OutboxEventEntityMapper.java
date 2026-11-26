package com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.mapper;

import com.warehouse.eventoutbox.outbox.domain.model.OutboxEvent;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.entity.OutboxEventEntity;

public class OutboxEventEntityMapper {

    public OutboxEventEntity toEntity(final OutboxEvent outboxEvent) {
        return OutboxEventEntity.builder()
                .eventId(outboxEvent.getEventId())
                .eventType(outboxEvent.getEventType())
                .eventVersion(outboxEvent.getEventVersion())
                .topic(outboxEvent.getTopic())
                .messageKey(outboxEvent.getMessageKey())
                .payload(outboxEvent.getPayload())
                .headers(outboxEvent.getHeaders())
                .occurredAt(outboxEvent.getOccurredAt())
                .status(outboxEvent.getStatus())
                .attemptCount(outboxEvent.getAttemptCount())
                .nextAttemptAt(outboxEvent.getNextAttemptAt())
                .lastError(outboxEvent.getLastError())
                .publishedAt(outboxEvent.getPublishedAt())
                .build();
    }

    public OutboxEventEntity withState(final OutboxEventEntity entity, final OutboxEvent outboxEvent) {
        return entity.toBuilder()
                .status(outboxEvent.getStatus())
                .attemptCount(outboxEvent.getAttemptCount())
                .nextAttemptAt(outboxEvent.getNextAttemptAt())
                .lastError(outboxEvent.getLastError())
                .publishedAt(outboxEvent.getPublishedAt())
                .build();
    }

    public OutboxEvent toDomain(final OutboxEventEntity entity) {
        return new OutboxEvent(
                entity.getEventId(),
                entity.getEventType(),
                entity.getEventVersion(),
                entity.getTopic(),
                entity.getMessageKey(),
                entity.getPayload(),
                entity.getHeaders(),
                entity.getOccurredAt(),
                entity.getStatus(),
                entity.getAttemptCount(),
                entity.getNextAttemptAt(),
                entity.getLastError(),
                entity.getPublishedAt());
    }
}
