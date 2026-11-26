package com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary;

import java.time.Clock;
import java.time.Duration;

import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxEventRepository;
import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxPublicationMetrics;
import com.warehouse.eventoutbox.outbox.domain.enumeration.OutboxEventStatus;
import com.warehouse.eventoutbox.outbox.domain.model.OutboxEvent;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

public class MicrometerOutboxPublicationMetrics implements OutboxPublicationMetrics {

    private static final String EVENTS_GAUGE = "manager.outbox.events";
    private static final String OLDEST_PENDING_GAUGE = "manager.outbox.oldest.pending.age";
    private static final String PUBLICATIONS_COUNTER = "manager.outbox.publications";

    private final MeterRegistry meterRegistry;

    public MicrometerOutboxPublicationMetrics(final MeterRegistry meterRegistry,
                                              final OutboxEventRepository repository,
                                              final Clock clock) {
        this.meterRegistry = meterRegistry;
        Gauge.builder(EVENTS_GAUGE, repository, outbox -> outbox.countByStatus(OutboxEventStatus.PENDING))
                .tag("status", "pending")
                .register(meterRegistry);
        Gauge.builder(EVENTS_GAUGE, repository, outbox -> outbox.countByStatus(OutboxEventStatus.DEAD))
                .tag("status", "dead")
                .register(meterRegistry);
        Gauge.builder(OLDEST_PENDING_GAUGE, repository, outbox -> outbox.findOldestPendingOccurredAt()
                        .map(occurredAt -> Duration.between(occurredAt, clock.instant()).toSeconds())
                        .orElse(0L))
                .baseUnit("seconds")
                .register(meterRegistry);
    }

    @Override
    public void published(final OutboxEvent outboxEvent) {
        increment(outboxEvent, "published");
    }

    @Override
    public void retryScheduled(final OutboxEvent outboxEvent) {
        increment(outboxEvent, "retry");
    }

    @Override
    public void dead(final OutboxEvent outboxEvent) {
        increment(outboxEvent, "dead");
    }

    private void increment(final OutboxEvent outboxEvent, final String outcome) {
        this.meterRegistry.counter(PUBLICATIONS_COUNTER, "event_type", outboxEvent.getEventType(), "outcome", outcome)
                .increment();
    }
}
