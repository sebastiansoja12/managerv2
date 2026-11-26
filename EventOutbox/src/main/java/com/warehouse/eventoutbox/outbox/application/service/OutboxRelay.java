package com.warehouse.eventoutbox.outbox.application.service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.transaction.support.TransactionOperations;

import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxDeadLetterPort;
import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxEventRepository;
import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxEventSenderServicePort;
import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxPublicationMetrics;
import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxRelayLock;
import com.warehouse.eventoutbox.outbox.domain.model.OutboxEvent;
import com.warehouse.eventoutbox.outbox.domain.service.OutboxRetryPolicy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OutboxRelay {

    private final OutboxEventRepository repository;
    private final OutboxRelayLock relayLock;
    private final OutboxEventSenderServicePort sender;
    private final OutboxDeadLetterPort deadLetter;
    private final OutboxRetryPolicy retryPolicy;
    private final OutboxPublicationMetrics metrics;
    private final TransactionOperations transactionOperations;
    private final Executor sendExecutor;
    private final Clock clock;
    private final int batchSize;
    private final Duration sendTimeout;

    public OutboxRelay(final OutboxEventRepository repository,
                       final OutboxRelayLock relayLock,
                       final OutboxEventSenderServicePort sender,
                       final OutboxDeadLetterPort deadLetter,
                       final OutboxRetryPolicy retryPolicy,
                       final OutboxPublicationMetrics metrics,
                       final TransactionOperations transactionOperations,
                       final Executor sendExecutor,
                       final Clock clock,
                       final int batchSize,
                       final Duration sendTimeout) {
        this.repository = repository;
        this.relayLock = relayLock;
        this.sender = sender;
        this.deadLetter = deadLetter;
        this.retryPolicy = retryPolicy;
        this.metrics = metrics;
        this.transactionOperations = transactionOperations;
        this.sendExecutor = sendExecutor;
        this.clock = clock;
        this.batchSize = batchSize;
        this.sendTimeout = sendTimeout;
    }

    public void publishPending() {
        int processed;
        do {
            processed = publishBatch();
        } while (processed == this.batchSize);
    }

    public int publishBatch() {
        final Integer processed = this.transactionOperations.execute(status -> {
            if (!this.relayLock.tryAcquire()) {
                return 0;
            }
            final List<OutboxEvent> events = this.repository.findPublishable(this.clock.instant(), this.batchSize);
            publish(events);
            return events.size();
        });
        return Objects.requireNonNullElse(processed, 0);
    }

    private void publish(final List<OutboxEvent> events) {
        final Map<String, List<OutboxEvent>> eventsByOrderingKey = events.stream()
                .collect(Collectors.groupingBy(OutboxEvent::orderingKey, LinkedHashMap::new, Collectors.toList()));
        final List<CompletableFuture<PublicationResult>> results = eventsByOrderingKey.values().stream()
                .map(this::sendInOrder)
                .toList();
        CompletableFuture.allOf(results.toArray(CompletableFuture[]::new)).join();

        final Instant now = this.clock.instant();
        final List<OutboxEvent> changed = new ArrayList<>();
        results.stream().map(CompletableFuture::join).forEach(result -> {
            result.published().forEach(event -> {
                event.markPublished(now);
                recordPublished(event);
                changed.add(event);
            });
            result.failure().ifPresent(failure -> {
                failure.event().registerFailedAttempt(this.retryPolicy, errorMessage(failure.exception()), now);
                recordFailure(failure.event(), failure.exception());
                changed.add(failure.event());
            });
        });
        this.repository.saveAll(changed);
        changed.stream().filter(OutboxEvent::isDead).forEach(this.deadLetter::publish);
    }

    private CompletableFuture<PublicationResult> sendInOrder(final List<OutboxEvent> orderedEvents) {
        CompletableFuture<PublicationResult> chain = CompletableFuture.completedFuture(PublicationResult.empty());
        for (final OutboxEvent event : orderedEvents) {
            chain = chain.thenComposeAsync(result -> result.failure().isPresent()
                    ? CompletableFuture.completedFuture(result)
                    : send(event, result), this.sendExecutor);
        }
        return chain;
    }

    private CompletableFuture<PublicationResult> send(final OutboxEvent event, final PublicationResult result) {
        return sendToBroker(event)
                .orTimeout(this.sendTimeout.toMillis(), TimeUnit.MILLISECONDS)
                .handle((ignored, exception) -> exception == null
                        ? result.withPublished(event)
                        : result.withFailure(new PublicationFailure(event, unwrap(exception))));
    }

    private CompletableFuture<Void> sendToBroker(final OutboxEvent event) {
        try {
            return this.sender.send(event);
        } catch (final RuntimeException exception) {
            return CompletableFuture.failedFuture(exception);
        }
    }

    private void recordPublished(final OutboxEvent event) {
        this.metrics.published(event);
        log.debug("[outbox] published eventId={} type={} topic={} key={}",
                event.getEventId().value(), event.getEventType(), event.getTopic(), event.getMessageKey());
    }

    private void recordFailure(final OutboxEvent event, final Throwable exception) {
        if (event.isDead()) {
            this.metrics.dead(event);
            log.error("[outbox] dead eventId={} type={} topic={} key={} attempt={}",
                    event.getEventId().value(), event.getEventType(), event.getTopic(), event.getMessageKey(),
                    event.getAttemptCount(), exception);
            return;
        }
        this.metrics.retryScheduled(event);
        log.warn("[outbox] failed eventId={} type={} topic={} key={} attempt={} nextAttemptAt={}",
                event.getEventId().value(), event.getEventType(), event.getTopic(), event.getMessageKey(),
                event.getAttemptCount(), event.getNextAttemptAt(), exception);
    }

    private Throwable unwrap(final Throwable exception) {
        return exception instanceof CompletionException && exception.getCause() != null
                ? exception.getCause()
                : exception;
    }

    private String errorMessage(final Throwable exception) {
        return exception.getMessage() == null
                ? exception.getClass().getName()
                : exception.getClass().getName() + ": " + exception.getMessage();
    }

    private record PublicationFailure(OutboxEvent event, Throwable exception) {
    }

    private record PublicationResult(List<OutboxEvent> published, Optional<PublicationFailure> failure) {

        private static PublicationResult empty() {
            return new PublicationResult(List.of(), Optional.empty());
        }

        private PublicationResult withPublished(final OutboxEvent event) {
            final List<OutboxEvent> events = new ArrayList<>(this.published);
            events.add(event);
            return new PublicationResult(List.copyOf(events), Optional.empty());
        }

        private PublicationResult withFailure(final PublicationFailure publicationFailure) {
            return new PublicationResult(this.published, Optional.of(publicationFailure));
        }
    }
}
