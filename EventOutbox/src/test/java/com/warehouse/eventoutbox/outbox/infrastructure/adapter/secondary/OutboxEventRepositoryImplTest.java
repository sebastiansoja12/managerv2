package com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary;

import static com.warehouse.eventoutbox.outbox.OutboxFixture.NOW;
import static com.warehouse.eventoutbox.outbox.OutboxFixture.pendingEvent;
import static com.warehouse.eventoutbox.outbox.OutboxFixture.retryPolicy;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ContextConfiguration;

import com.warehouse.eventoutbox.outbox.OutboxPersistenceTestConfiguration;
import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxEventRepository;
import com.warehouse.eventoutbox.outbox.domain.enumeration.OutboxEventStatus;
import com.warehouse.eventoutbox.outbox.domain.model.OutboxEvent;
import com.warehouse.eventoutbox.outbox.domain.service.OutboxRetryPolicy;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ContextConfiguration(classes = OutboxPersistenceTestConfiguration.class)
class OutboxEventRepositoryImplTest {

    @Autowired
    private OutboxEventRepository repository;

    @Test
    void shouldReturnPendingEventsInInsertionOrderWithHeaders() {
        final OutboxEvent first = pendingEvent("shipment-1");
        final OutboxEvent second = pendingEvent("shipment-2");
        this.repository.save(first);
        this.repository.save(second);

        final List<OutboxEvent> publishable = this.repository.findPublishable(NOW, 10);

        assertThat(publishable).extracting(OutboxEvent::getEventId).containsExactly(first.getEventId(), second.getEventId());
        assertThat(publishable.getFirst().getHeaders()).containsEntry("ce_type", "shipment.created");
    }

    @Test
    void shouldBlockLaterEventsWithSameKeyWhileEarlierEventWaitsForRetry() {
        final OutboxEvent waiting = pendingEvent("shipment-1");
        final OutboxEvent blocked = pendingEvent("shipment-1");
        final OutboxEvent otherKey = pendingEvent("shipment-2");
        this.repository.save(waiting);
        this.repository.save(blocked);
        this.repository.save(otherKey);
        waiting.registerFailedAttempt(retryPolicy(), "broker unavailable", NOW);
        this.repository.saveAll(List.of(waiting));

        final List<OutboxEvent> publishable = this.repository.findPublishable(NOW, 10);

        assertThat(publishable).extracting(OutboxEvent::getEventId).containsExactly(otherKey.getEventId());
    }

    @Test
    void shouldBlockLaterEventsWithSameKeyWhenEarlierEventIsDead() {
        final OutboxEvent dead = pendingEvent("shipment-1");
        final OutboxEvent blocked = pendingEvent("shipment-1");
        final OutboxEvent unkeyed = pendingEvent(null);
        this.repository.save(dead);
        this.repository.save(blocked);
        this.repository.save(unkeyed);
        dead.registerFailedAttempt(new OutboxRetryPolicy(Duration.ofSeconds(5), Duration.ofSeconds(5), 1),
                "serialization failed", NOW);
        this.repository.saveAll(List.of(dead));

        final List<OutboxEvent> publishable = this.repository.findPublishable(NOW.plusSeconds(3600), 10);

        assertThat(publishable).extracting(OutboxEvent::getEventId).containsExactly(unkeyed.getEventId());
        assertThat(this.repository.countByStatus(OutboxEventStatus.DEAD)).isEqualTo(1);
    }

    @Test
    void shouldReturnEventWaitingForRetryWhenItsAttemptTimeHasCome() {
        final OutboxEvent retried = pendingEvent("shipment-1");
        final OutboxEvent next = pendingEvent("shipment-1");
        this.repository.save(retried);
        this.repository.save(next);
        retried.registerFailedAttempt(retryPolicy(), "broker unavailable", NOW);
        this.repository.saveAll(List.of(retried));

        final List<OutboxEvent> publishable = this.repository.findPublishable(NOW.plusSeconds(5), 10);

        assertThat(publishable).extracting(OutboxEvent::getEventId).containsExactly(retried.getEventId(), next.getEventId());
        assertThat(publishable.getFirst().getAttemptCount()).isEqualTo(1);
        assertThat(publishable.getFirst().getLastError()).isEqualTo("broker unavailable");
    }

    @Test
    void shouldLimitNumberOfReturnedEvents() {
        this.repository.save(pendingEvent("shipment-1"));
        this.repository.save(pendingEvent("shipment-2"));
        this.repository.save(pendingEvent("shipment-3"));

        assertThat(this.repository.findPublishable(NOW, 2)).hasSize(2);
    }

    @Test
    void shouldDeleteOnlyPublishedEventsOlderThanThreshold() {
        final OutboxEvent oldPublished = pendingEvent("shipment-1");
        final OutboxEvent recentPublished = pendingEvent("shipment-2");
        final OutboxEvent pending = pendingEvent("shipment-3");
        this.repository.save(oldPublished);
        this.repository.save(recentPublished);
        this.repository.save(pending);
        oldPublished.markPublished(NOW.minus(Duration.ofDays(8)));
        recentPublished.markPublished(NOW);
        this.repository.saveAll(List.of(oldPublished, recentPublished));

        final int deleted = this.repository.deletePublishedBefore(NOW.minus(Duration.ofDays(7)), 100);

        assertThat(deleted).isEqualTo(1);
        assertThat(this.repository.countByStatus(OutboxEventStatus.PUBLISHED)).isEqualTo(1);
        assertThat(this.repository.countByStatus(OutboxEventStatus.PENDING)).isEqualTo(1);
    }

    @Test
    void shouldReturnOldestPendingOccurredAt() {
        this.repository.save(pendingEvent("shipment-1", NOW.minusSeconds(120)));
        this.repository.save(pendingEvent("shipment-2", NOW));

        assertThat(this.repository.findOldestPendingOccurredAt()).contains(NOW.minusSeconds(120));
        assertThat(this.repository.countByStatus(OutboxEventStatus.PENDING)).isEqualTo(2);
    }
}
