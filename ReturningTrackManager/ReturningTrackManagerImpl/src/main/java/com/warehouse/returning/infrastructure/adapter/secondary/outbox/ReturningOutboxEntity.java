package com.warehouse.returning.infrastructure.adapter.secondary.outbox;

import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ReturnId;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "returning_event_outbox")
public class ReturningOutboxEntity {

    @Id
    @Column(name = "event_id", nullable = false)
    private UUID eventId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "return_package_id", nullable = false))
    private ReturnId returnPackageId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "topic", nullable = false)
    private String topic;

    @Column(name = "message_key", nullable = false)
    private String messageKey;

    @Column(name = "payload_json", nullable = false, columnDefinition = "text")
    private String payload;

    @Column(name = "headers_json", nullable = false, columnDefinition = "text")
    private String headers;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReturningOutboxStatus status;

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "next_attempt_at")
    private Instant nextAttemptAt;

    @Column(name = "locked_until")
    private Instant lockedUntil;

    @Column(name = "lock_token")
    private UUID lockToken;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "last_error", columnDefinition = "text")
    private String lastError;

    protected ReturningOutboxEntity() {
    }

    public ReturningOutboxEntity(final UUID eventId, final ReturnPackageId returnPackageId,
                                  final String eventType, final String topic, final String messageKey,
                                  final String payload, final String headers, final Instant createdAt) {
        this.eventId = eventId;
        this.returnPackageId = ReturnId.of(returnPackageId);
        this.eventType = eventType;
        this.topic = topic;
        this.messageKey = messageKey;
        this.payload = payload;
        this.headers = headers;
        this.createdAt = createdAt;
        this.status = ReturningOutboxStatus.PENDING;
    }

    public ReturningOutboxMessage claim(final Instant lockedUntil) {
        this.status = ReturningOutboxStatus.PROCESSING;
        this.lockToken = UUID.randomUUID();
        this.lockedUntil = lockedUntil;
        this.attemptCount++;
        return new ReturningOutboxMessage(eventId, topic, messageKey, payload, headers, attemptCount, lockToken);
    }
}
