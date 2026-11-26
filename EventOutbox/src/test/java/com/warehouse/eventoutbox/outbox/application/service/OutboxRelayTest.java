package com.warehouse.eventoutbox.outbox.application.service;

import static com.warehouse.eventoutbox.outbox.OutboxFixture.NOW;
import static com.warehouse.eventoutbox.outbox.OutboxFixture.pendingEvent;
import static com.warehouse.eventoutbox.outbox.OutboxFixture.retryPolicy;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Duration;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.warehouse.eventoutbox.outbox.OutboxPersistenceTestConfiguration;
import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxEventRepository;
import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxEventSenderServicePort;
import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxRelayLock;
import com.warehouse.eventoutbox.outbox.domain.enumeration.OutboxEventStatus;
import com.warehouse.eventoutbox.outbox.domain.model.OutboxEvent;
import com.warehouse.eventoutbox.outbox.domain.service.OutboxRetryPolicy;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.MicrometerOutboxPublicationMetrics;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.OutboxEventJpaRepository;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.entity.OutboxEventEntity;
import com.warehouse.eventoutbox.integration.model.EventId;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ContextConfiguration(classes = OutboxPersistenceTestConfiguration.class)
class OutboxRelayTest {

    private static final Clock CLOCK = Clock.fixed(NOW, ZoneOffset.UTC);

    @Autowired
    private OutboxEventRepository repository;

    @Autowired
    private OutboxEventJpaRepository jpaRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private SimpleMeterRegistry meterRegistry;

    private final List<OutboxEvent> deadLetters = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.meterRegistry = new SimpleMeterRegistry();
    }

    @Test
    void shouldPublishPendingEventsAndMarkThemAsPublished() {
        final OutboxEvent first = pendingEvent("shipment-1");
        final OutboxEvent second = pendingEvent("shipment-1");
        this.repository.save(first);
        this.repository.save(second);
        final RecordingSender sender = new RecordingSender(Set.of());

        relay(sender, () -> true, 10).publishPending();

        assertThat(sender.sent).extracting(OutboxEvent::getEventId).containsExactly(first.getEventId(), second.getEventId());
        assertThat(stored(first).getStatus()).isEqualTo(OutboxEventStatus.PUBLISHED);
        assertThat(stored(first).getPublishedAt()).isEqualTo(NOW);
        assertThat(stored(second).getStatus()).isEqualTo(OutboxEventStatus.PUBLISHED);
        assertThat(this.meterRegistry.get("manager.outbox.publications").tag("outcome", "published").counter()
                .count()).isEqualTo(2);
    }

    @Test
    void shouldStopKeyAfterFailureAndContinueWithOtherKeys() {
        final OutboxEvent failing = pendingEvent("shipment-1");
        final OutboxEvent blocked = pendingEvent("shipment-1");
        final OutboxEvent otherKey = pendingEvent("shipment-2");
        this.repository.save(failing);
        this.repository.save(blocked);
        this.repository.save(otherKey);
        final RecordingSender sender = new RecordingSender(Set.of(failing.getEventId()));

        relay(sender, () -> true, 10).publishPending();

        assertThat(sender.sent).extracting(OutboxEvent::getEventId)
                .containsExactly(failing.getEventId(), otherKey.getEventId());
        assertThat(stored(failing).getStatus()).isEqualTo(OutboxEventStatus.PENDING);
        assertThat(stored(failing).getAttemptCount()).isEqualTo(1);
        assertThat(stored(failing).getLastError()).contains("broker unavailable");
        assertThat(stored(blocked).getStatus()).isEqualTo(OutboxEventStatus.PENDING);
        assertThat(stored(blocked).getAttemptCount()).isZero();
        assertThat(stored(otherKey).getStatus()).isEqualTo(OutboxEventStatus.PUBLISHED);
        assertThat(this.repository.findPublishable(NOW, 10)).isEmpty();
        assertThat(this.deadLetters).isEmpty();
    }

    @Test
    void shouldMarkEventAsDeadWhenMaxAttemptsAreReached() {
        final OutboxEvent failing = pendingEvent("shipment-1");
        this.repository.save(failing);
        final RecordingSender sender = new RecordingSender(Set.of(failing.getEventId()));

        relay(sender, () -> true, 10, new OutboxRetryPolicy(Duration.ofSeconds(5), Duration.ofSeconds(5), 1),
                Duration.ofSeconds(1)).publishPending();

        assertThat(stored(failing).getStatus()).isEqualTo(OutboxEventStatus.DEAD);
        assertThat(stored(failing).getAttemptCount()).isEqualTo(1);
        assertThat(this.meterRegistry.get("manager.outbox.publications").tag("outcome", "dead").counter()
                .count()).isEqualTo(1);
        assertThat(this.deadLetters).extracting(OutboxEvent::getEventId).containsExactly(failing.getEventId());
    }

    @Test
    void shouldScheduleRetryWhenBrokerDoesNotAcknowledgeInTime() {
        final OutboxEvent unacknowledged = pendingEvent("shipment-1");
        this.repository.save(unacknowledged);
        final OutboxEventSenderServicePort neverAcknowledging = outboxEvent -> new CompletableFuture<>();

        relay(neverAcknowledging, () -> true, 10, retryPolicy(), Duration.ofMillis(50)).publishPending();

        assertThat(stored(unacknowledged).getStatus()).isEqualTo(OutboxEventStatus.PENDING);
        assertThat(stored(unacknowledged).getAttemptCount()).isEqualTo(1);
        assertThat(stored(unacknowledged).getLastError()).contains("TimeoutException");
    }

    @Test
    void shouldNotPublishWhenRelayLockIsHeldByAnotherInstance() {
        final OutboxEvent pending = pendingEvent("shipment-1");
        this.repository.save(pending);
        final RecordingSender sender = new RecordingSender(Set.of());

        relay(sender, () -> false, 10).publishPending();

        assertThat(sender.sent).isEmpty();
        assertThat(stored(pending).getStatus()).isEqualTo(OutboxEventStatus.PENDING);
    }

    @Test
    void shouldPublishAllEventsInConsecutiveBatches() {
        final OutboxEvent first = pendingEvent("shipment-1");
        final OutboxEvent second = pendingEvent("shipment-2");
        final OutboxEvent third = pendingEvent("shipment-3");
        this.repository.save(first);
        this.repository.save(second);
        this.repository.save(third);
        final RecordingSender sender = new RecordingSender(Set.of());

        relay(sender, () -> true, 2).publishPending();

        assertThat(sender.sent).hasSize(3);
        assertThat(stored(third).getStatus()).isEqualTo(OutboxEventStatus.PUBLISHED);
    }

    private OutboxEventEntity stored(final OutboxEvent event) {
        return this.jpaRepository.findAllByEventIdIn(List.of(event.getEventId())).getFirst();
    }

    private OutboxRelay relay(final OutboxEventSenderServicePort sender,
                              final OutboxRelayLock relayLock,
                              final int batchSize) {
        return relay(sender, relayLock, batchSize, retryPolicy(), Duration.ofSeconds(1));
    }

    private OutboxRelay relay(final OutboxEventSenderServicePort sender,
                              final OutboxRelayLock relayLock,
                              final int batchSize,
                              final OutboxRetryPolicy retryPolicy,
                              final Duration sendTimeout) {
        return new OutboxRelay(
                this.repository,
                relayLock,
                sender,
                this.deadLetters::add,
                retryPolicy,
                new MicrometerOutboxPublicationMetrics(this.meterRegistry, this.repository, CLOCK),
                new TransactionTemplate(this.transactionManager),
                Runnable::run,
                CLOCK,
                batchSize,
                sendTimeout);
    }

    private static final class RecordingSender implements OutboxEventSenderServicePort {

        private final List<OutboxEvent> sent = new ArrayList<>();
        private final Set<EventId> failingEventIds;

        private RecordingSender(final Set<EventId> failingEventIds) {
            this.failingEventIds = failingEventIds;
        }

        @Override
        public CompletableFuture<Void> send(final OutboxEvent outboxEvent) {
            this.sent.add(outboxEvent);
            if (this.failingEventIds.contains(outboxEvent.getEventId())) {
                return CompletableFuture.failedFuture(new IllegalStateException("broker unavailable"));
            }
            return CompletableFuture.completedFuture(null);
        }
    }
}
