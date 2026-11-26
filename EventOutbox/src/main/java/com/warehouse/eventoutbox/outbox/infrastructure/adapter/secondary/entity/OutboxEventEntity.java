package com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.entity;

import java.time.Instant;
import java.util.Map;

import com.warehouse.eventoutbox.outbox.domain.enumeration.OutboxEventStatus;
import com.warehouse.eventoutbox.integration.model.EventId;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "integration_event_outbox")
@Getter
public class OutboxEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sequence_number", nullable = false)
    private Long sequenceNumber;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "event_id", nullable = false, unique = true, length = 36))
    private EventId eventId;

    @Column(name = "event_type", nullable = false, length = 128)
    private String eventType;

    @Column(name = "event_version", nullable = false)
    private int eventVersion;

    @Column(name = "topic", nullable = false)
    private String topic;

    @Column(name = "message_key")
    private String messageKey;

    @Column(name = "payload", nullable = false, columnDefinition = "text")
    private String payload;

    @Convert(converter = OutboxEventHeadersConverter.class)
    @Column(name = "headers", nullable = false, columnDefinition = "text")
    private Map<String, String> headers;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private OutboxEventStatus status;

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount;

    @Column(name = "next_attempt_at", nullable = false)
    private Instant nextAttemptAt;

    @Column(name = "last_error", columnDefinition = "text")
    private String lastError;

    @Column(name = "published_at")
    private Instant publishedAt;

    protected OutboxEventEntity() {
    }

    @Builder(toBuilder = true)
    private OutboxEventEntity(final Long sequenceNumber,
                              final EventId eventId,
                              final String eventType,
                              final int eventVersion,
                              final String topic,
                              final String messageKey,
                              final String payload,
                              final Map<String, String> headers,
                              final Instant occurredAt,
                              final OutboxEventStatus status,
                              final int attemptCount,
                              final Instant nextAttemptAt,
                              final String lastError,
                              final Instant publishedAt) {
        this.sequenceNumber = sequenceNumber;
        this.eventId = eventId;
        this.eventType = eventType;
        this.eventVersion = eventVersion;
        this.topic = topic;
        this.messageKey = messageKey;
        this.payload = payload;
        this.headers = headers;
        this.occurredAt = occurredAt;
        this.status = status;
        this.attemptCount = attemptCount;
        this.nextAttemptAt = nextAttemptAt;
        this.lastError = lastError;
        this.publishedAt = publishedAt;
    }
}
