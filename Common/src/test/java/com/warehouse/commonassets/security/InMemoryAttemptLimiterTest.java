package com.warehouse.commonassets.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

class InMemoryAttemptLimiterTest {

    private static final Instant NOW = Instant.parse("2026-11-10T10:00:00Z");

    @Test
    void shouldBlockKeyAfterMaxAttemptsAndKeepOtherKeysOpen() {
        final AttemptLimiter limiter = new InMemoryAttemptLimiter(2, Duration.ofMinutes(15),
                Clock.fixed(NOW, ZoneOffset.UTC));

        limiter.recordAttempt("a");
        assertThat(limiter.isBlocked("a")).isFalse();
        limiter.recordAttempt("a");

        assertThat(limiter.isBlocked("a")).isTrue();
        assertThat(limiter.isBlocked("b")).isFalse();
    }

    @Test
    void shouldUnblockKeyAfterResetOrWhenWindowPasses() {
        final MutableClock clock = new MutableClock(NOW);
        final AttemptLimiter limiter = new InMemoryAttemptLimiter(1, Duration.ofMinutes(15), clock);

        limiter.recordAttempt("a");
        limiter.recordAttempt("b");
        limiter.reset("a");
        assertThat(limiter.isBlocked("a")).isFalse();
        assertThat(limiter.isBlocked("b")).isTrue();

        clock.now = NOW.plus(Duration.ofMinutes(16));
        assertThat(limiter.isBlocked("b")).isFalse();
    }

    private static final class MutableClock extends Clock {

        private Instant now;

        private MutableClock(final Instant now) {
            this.now = now;
        }

        @Override
        public java.time.ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(final java.time.ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return this.now;
        }
    }
}
