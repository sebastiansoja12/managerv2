package com.warehouse.eventoutbox.outbox.domain.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class OutboxRetryPolicy {

    private static final int MAX_BACKOFF_EXPONENT = 30;

    private final Duration initialDelay;
    private final Duration maxDelay;
    private final int maxAttempts;

    public OutboxRetryPolicy(final Duration initialDelay, final Duration maxDelay, final int maxAttempts) {
        if (initialDelay.isNegative() || initialDelay.isZero() || maxDelay.compareTo(initialDelay) < 0) {
            throw new IllegalArgumentException(
                    "Outbox retry initial delay must be positive and not greater than maximum delay");
        }
        if (maxAttempts <= 0) {
            throw new IllegalArgumentException("Outbox retry max attempts must be positive");
        }
        this.initialDelay = initialDelay;
        this.maxDelay = maxDelay;
        this.maxAttempts = maxAttempts;
    }

    public Optional<Instant> nextAttemptAt(final int failedAttempts, final Instant now) {
        if (failedAttempts >= this.maxAttempts) {
            return Optional.empty();
        }
        final long multiplier = 1L << Math.min(failedAttempts - 1, MAX_BACKOFF_EXPONENT);
        final Duration delay = this.initialDelay.multipliedBy(multiplier);
        return Optional.of(now.plus(delay.compareTo(this.maxDelay) > 0 ? this.maxDelay : delay));
    }
}
