package com.warehouse.eventoutbox.outbox.domain.model;

import java.time.Instant;
import java.util.Map;

import com.warehouse.eventoutbox.outbox.domain.enumeration.OutboxEventStatus;
import com.warehouse.eventoutbox.outbox.domain.service.OutboxRetryPolicy;
import com.warehouse.eventoutbox.integration.model.EventId;

import lombok.Getter;

@Getter
public class OutboxEvent {

    private final EventId eventId;
    private final String eventType;
    private final int eventVersion;
    private final String topic;
    private final String messageKey;
    private final String payload;
    private final Map<String, String> headers;
    private final Instant occurredAt;
    private OutboxEventStatus status;
    private int attemptCount;
    private Instant nextAttemptAt;
    private String lastError;
    private Instant publishedAt;

    public OutboxEvent(final EventId eventId,
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
        this.eventId = eventId;
        this.eventType = eventType;
        this.eventVersion = eventVersion;
        this.topic = topic;
        this.messageKey = messageKey;
        this.payload = payload;
        this.headers = Map.copyOf(headers);
        this.occurredAt = occurredAt;
        this.status = status;
        this.attemptCount = attemptCount;
        this.nextAttemptAt = nextAttemptAt;
        this.lastError = lastError;
        this.publishedAt = publishedAt;
    }

    public void markPublished(final Instant publishedAt) {
        requirePending("published");
        this.status = OutboxEventStatus.PUBLISHED;
        this.publishedAt = publishedAt;
        this.lastError = null;
    }

    public void registerFailedAttempt(final OutboxRetryPolicy retryPolicy, final String error, final Instant now) {
        requirePending("retried");
        this.attemptCount++;
        this.lastError = error;
        retryPolicy.nextAttemptAt(this.attemptCount, now).ifPresentOrElse(
                nextAttempt -> this.nextAttemptAt = nextAttempt,
                () -> this.status = OutboxEventStatus.DEAD);
    }

    public boolean isDead() {
        return this.status == OutboxEventStatus.DEAD;
    }

    public String orderingKey() {
        return this.messageKey == null ? "event:" + this.eventId.value() : "key:" + this.messageKey;
    }

    private void requirePending(final String transition) {
        if (this.status != OutboxEventStatus.PENDING) {
            throw new IllegalStateException("Outbox event " + this.eventId.value() + " cannot be " + transition
                    + " in status " + this.status);
        }
    }
}
