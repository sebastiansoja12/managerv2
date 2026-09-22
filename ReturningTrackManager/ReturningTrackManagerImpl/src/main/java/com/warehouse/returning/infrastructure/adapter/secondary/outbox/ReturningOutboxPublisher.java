package com.warehouse.returning.infrastructure.adapter.secondary.outbox;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class ReturningOutboxPublisher {

    private static final Logger log = LoggerFactory.getLogger(ReturningOutboxPublisher.class);
    private static final TypeReference<Map<String, String>> HEADERS_TYPE = new TypeReference<>() { };

    private final ReturningOutboxStore store;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final int batchSize;
    private final long lockDurationMs;
    private final long sendTimeoutMs;
    private final long retryDelayMs;
    private final int maxAttempts;

    public ReturningOutboxPublisher(final ReturningOutboxStore store,
                                    final KafkaTemplate<String, String> kafkaTemplate,
                                    final ObjectMapper objectMapper,
                                    @Value("${returning.outbox.batch-size:50}") final int batchSize,
                                    @Value("${returning.outbox.lock-duration-ms:60000}") final long lockDurationMs,
                                    @Value("${returning.outbox.send-timeout-ms:10000}") final long sendTimeoutMs,
                                    @Value("${returning.outbox.retry-delay-ms:5000}") final long retryDelayMs,
                                    @Value("${returning.outbox.max-attempts:10}") final int maxAttempts) {
        this.store = store;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.batchSize = batchSize;
        this.lockDurationMs = lockDurationMs;
        this.sendTimeoutMs = sendTimeoutMs;
        this.retryDelayMs = retryDelayMs;
        this.maxAttempts = maxAttempts;
    }

    @Scheduled(fixedDelayString = "${returning.outbox.publish-delay-ms:5000}")
    public void publishPending() {
        for (int index = 0; index < batchSize && !Thread.currentThread().isInterrupted(); index++) {
            final Instant now = Instant.now();
            final Optional<ReturningOutboxMessage> message = store.claim(now, now.plusMillis(lockDurationMs));
            if (message.isEmpty()) {
                return;
            }
            message.ifPresent(this::publish);
        }
    }

    private void publish(final ReturningOutboxMessage message) {
        try {
            final ProducerRecord<String, String> record = new ProducerRecord<>(
                    message.topic(), message.messageKey(), message.payload());
            final Map<String, String> headers = objectMapper.readValue(message.headers(), HEADERS_TYPE);
            headers.forEach((name, value) -> record.headers().add(name, value.getBytes(StandardCharsets.UTF_8)));
            kafkaTemplate.send(record).get(sendTimeoutMs, TimeUnit.MILLISECONDS);
            store.markPublished(message);
        } catch (final InterruptedException exception) {
            Thread.currentThread().interrupt();
            handleFailure(message, exception);
        } catch (final Exception exception) {
            handleFailure(message, exception);
        }
    }

    private void handleFailure(final ReturningOutboxMessage message, final Exception exception) {
        final int exponent = Math.min(message.attemptCount() - 1, 10);
        final Instant nextAttemptAt = Instant.now().plusMillis(Math.multiplyExact(retryDelayMs, 1L << exponent));
        store.markFailed(message, exception, nextAttemptAt, maxAttempts);
        log.error("Cannot publish RTM outbox event {} (attempt {}, exhausted={})",
                message.eventId(), message.attemptCount(), message.attemptCount() >= maxAttempts, exception);
    }
}
