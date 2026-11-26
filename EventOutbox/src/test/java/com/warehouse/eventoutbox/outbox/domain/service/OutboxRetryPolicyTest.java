package com.warehouse.eventoutbox.outbox.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.Test;

class OutboxRetryPolicyTest {

    private static final Instant NOW = Instant.parse("2026-11-18T10:00:00Z");

    @Test
    void shouldDoubleDelayUntilMaximumDelay() {
        final OutboxRetryPolicy policy = new OutboxRetryPolicy(Duration.ofSeconds(5), Duration.ofSeconds(60), 10);

        assertThat(policy.nextAttemptAt(1, NOW)).contains(NOW.plusSeconds(5));
        assertThat(policy.nextAttemptAt(2, NOW)).contains(NOW.plusSeconds(10));
        assertThat(policy.nextAttemptAt(3, NOW)).contains(NOW.plusSeconds(20));
        assertThat(policy.nextAttemptAt(5, NOW)).contains(NOW.plusSeconds(60));
        assertThat(policy.nextAttemptAt(9, NOW)).contains(NOW.plusSeconds(60));
    }

    @Test
    void shouldReturnEmptyWhenMaxAttemptsAreReached() {
        final OutboxRetryPolicy policy = new OutboxRetryPolicy(Duration.ofSeconds(5), Duration.ofSeconds(60), 3);

        assertThat(policy.nextAttemptAt(2, NOW)).isPresent();
        assertThat(policy.nextAttemptAt(3, NOW)).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenMaximumDelayIsShorterThanInitialDelay() {
        assertThatThrownBy(() -> new OutboxRetryPolicy(Duration.ofSeconds(10), Duration.ofSeconds(5), 3))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
