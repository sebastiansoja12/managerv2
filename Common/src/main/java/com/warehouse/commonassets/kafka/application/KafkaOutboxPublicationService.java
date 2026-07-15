package com.warehouse.commonassets.kafka.application;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxRecord;
import com.warehouse.commonassets.kafka.domain.port.KafkaMessagePublisherPort;
import com.warehouse.commonassets.kafka.domain.port.KafkaOutboxPort;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "manager.kafka.outbox.enabled", havingValue = "true")
public class KafkaOutboxPublicationService {

    private final KafkaOutboxPort outboxPort;
    private final KafkaMessagePublisherPort messagePublisherPort;
    private final int batchSize;
    private final long lockDurationMs;
    private final long retryDelayMs;
    private final int maxAttempts;
    private final String workerId;

    public KafkaOutboxPublicationService(final KafkaOutboxPort outboxPort,
                                         final KafkaMessagePublisherPort messagePublisherPort,
                                         @Value("${manager.kafka.outbox.batch-size:50}") final int batchSize,
                                         @Value("${manager.kafka.outbox.lock-duration-ms:30000}") final long lockDurationMs,
                                         @Value("${manager.kafka.outbox.retry-delay-ms:5000}") final long retryDelayMs,
                                         @Value("${manager.kafka.outbox.max-attempts:10}") final int maxAttempts) {
        this.outboxPort = outboxPort;
        this.messagePublisherPort = messagePublisherPort;
        this.batchSize = batchSize;
        this.lockDurationMs = lockDurationMs;
        this.retryDelayMs = retryDelayMs;
        this.maxAttempts = maxAttempts;
        this.workerId = UUID.randomUUID().toString();
    }

    @Scheduled(fixedDelayString = "${manager.kafka.outbox.publish-delay-ms:5000}")
    public void publishPending() {
        final Instant now = Instant.now();
        final List<KafkaOutboxRecord> records = this.outboxPort.claimPending(
                this.batchSize, this.workerId, now, now.plusMillis(this.lockDurationMs));
        records.forEach(this::publishClaimed);
    }

    public void publish(final KafkaOutboxRecord record) {
        final Instant now = Instant.now();
        if (!this.outboxPort.claim(record.eventId(), this.workerId, now, now.plusMillis(this.lockDurationMs))) {
            return;
        }
        publishClaimed(record);
    }

    private void publishClaimed(final KafkaOutboxRecord record) {
        try {
            this.messagePublisherPort.publish(record.topic(), record.messageKey(), record.payload(), record.headers())
                    .whenComplete((result, exception) -> {
                        if (exception != null) {
                            handleFailure(record, exception);
                            return;
                        }

                        this.outboxPort.markPublished(record.eventId(), this.workerId);
                    });
        } catch (final RuntimeException exception) {
            handleFailure(record, exception);
        }
    }

    private void handleFailure(final KafkaOutboxRecord record, final Throwable exception) {
        this.outboxPort.markFailed(
                record.eventId(),
                this.workerId,
                exception,
                nextAttemptAt(record),
                this.maxAttempts);
        log.error("Cannot publish outbox Kafka event {}", record.eventId(), exception);
    }

    private Instant nextAttemptAt(final KafkaOutboxRecord record) {
        final int exponent = Math.min(record.attemptCount(), 10);
        final long delay = Math.multiplyExact(this.retryDelayMs, 1L << exponent);
        return Instant.now().plusMillis(delay);
    }
}
