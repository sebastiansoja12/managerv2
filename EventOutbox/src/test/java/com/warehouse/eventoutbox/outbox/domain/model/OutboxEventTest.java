package com.warehouse.eventoutbox.outbox.domain.model;

import static com.warehouse.eventoutbox.outbox.OutboxFixture.NOW;
import static com.warehouse.eventoutbox.outbox.OutboxFixture.pendingEvent;
import static com.warehouse.eventoutbox.outbox.OutboxFixture.retryPolicy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import com.warehouse.eventoutbox.outbox.domain.enumeration.OutboxEventStatus;
import com.warehouse.eventoutbox.outbox.domain.service.OutboxRetryPolicy;

class OutboxEventTest {

    @Test
    void shouldBecomePublishedAndClearLastError() {
        final OutboxEvent event = pendingEvent("SH-1");
        event.registerFailedAttempt(retryPolicy(), "broker unavailable", NOW);

        event.markPublished(NOW.plusSeconds(10));

        assertThat(event.getStatus()).isEqualTo(OutboxEventStatus.PUBLISHED);
        assertThat(event.getPublishedAt()).isEqualTo(NOW.plusSeconds(10));
        assertThat(event.getLastError()).isNull();
    }

    @Test
    void shouldScheduleNextAttemptAfterFailure() {
        final OutboxEvent event = pendingEvent("SH-1");

        event.registerFailedAttempt(retryPolicy(), "broker unavailable", NOW);

        assertThat(event.getStatus()).isEqualTo(OutboxEventStatus.PENDING);
        assertThat(event.getAttemptCount()).isEqualTo(1);
        assertThat(event.getNextAttemptAt()).isEqualTo(NOW.plusSeconds(5));
        assertThat(event.getLastError()).isEqualTo("broker unavailable");
    }

    @Test
    void shouldBecomeDeadWhenRetryPolicyIsExhausted() {
        final OutboxEvent event = pendingEvent("SH-1");
        final OutboxRetryPolicy singleAttempt = new OutboxRetryPolicy(Duration.ofSeconds(5), Duration.ofSeconds(5), 1);

        event.registerFailedAttempt(singleAttempt, "serialization failed", NOW);

        assertThat(event.isDead()).isTrue();
        assertThat(event.getAttemptCount()).isEqualTo(1);
        assertThat(event.getLastError()).isEqualTo("serialization failed");
    }

    @Test
    void shouldRejectPublishingDeadEvent() {
        final OutboxEvent event = pendingEvent("SH-1");
        event.registerFailedAttempt(new OutboxRetryPolicy(Duration.ofSeconds(5), Duration.ofSeconds(5), 1),
                "serialization failed", NOW);

        assertThatThrownBy(() -> event.markPublished(NOW))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("DEAD");
    }

    @Test
    void shouldRejectRetryingPublishedEvent() {
        final OutboxEvent event = pendingEvent("SH-1");
        event.markPublished(NOW);

        assertThatThrownBy(() -> event.registerFailedAttempt(retryPolicy(), "broker unavailable", NOW))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("PUBLISHED");
    }

    @Test
    void shouldOrderByMessageKeyOrByEventIdWhenKeyIsMissing() {
        final OutboxEvent keyed = pendingEvent("SH-1");
        final OutboxEvent unkeyed = pendingEvent(null);

        assertThat(keyed.orderingKey()).isEqualTo("key:SH-1");
        assertThat(unkeyed.orderingKey()).isEqualTo("event:" + unkeyed.getEventId().value());
    }
}
