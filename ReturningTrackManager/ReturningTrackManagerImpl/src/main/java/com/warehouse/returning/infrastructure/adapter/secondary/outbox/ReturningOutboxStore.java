package com.warehouse.returning.infrastructure.adapter.secondary.outbox;

import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Component
public class ReturningOutboxStore {

    private final ReturningOutboxRepository repository;

    public ReturningOutboxStore(final ReturningOutboxRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Optional<ReturningOutboxMessage> claim(final Instant now, final Instant lockedUntil) {
        return repository.findNext(now).map(entity -> entity.claim(lockedUntil));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markPublished(final ReturningOutboxMessage message) {
        repository.markPublished(message.eventId(), message.lockToken(), KafkaOutboxStatus.PUBLISHED, Instant.now());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailed(final ReturningOutboxMessage message, final Throwable exception,
                           final Instant nextAttemptAt, final int maxAttempts) {
        final boolean exhausted = message.attemptCount() >= maxAttempts;
        repository.markFailed(message.eventId(), message.lockToken(),
                exhausted ? KafkaOutboxStatus.DEAD : KafkaOutboxStatus.PENDING,
                exception.toString(), exhausted ? null : nextAttemptAt);
    }
}
