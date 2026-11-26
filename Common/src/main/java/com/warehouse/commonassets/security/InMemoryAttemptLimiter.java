package com.warehouse.commonassets.security;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAttemptLimiter implements AttemptLimiter {

    private static final int CLEANUP_THRESHOLD = 10_000;

    private final int maxAttempts;

    private final Duration window;

    private final Clock clock;

    private final Map<String, Window> windows = new ConcurrentHashMap<>();

    public InMemoryAttemptLimiter(final int maxAttempts, final Duration window, final Clock clock) {
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("Max attempts must be positive");
        }
        this.maxAttempts = maxAttempts;
        this.window = window;
        this.clock = clock;
    }

    @Override
    public boolean isBlocked(final String key) {
        final Window current = this.windows.get(key);
        return current != null && current.isActive(this.clock.instant()) && current.attempts() >= this.maxAttempts;
    }

    @Override
    public void recordAttempt(final String key) {
        final Instant now = this.clock.instant();
        if (this.windows.size() > CLEANUP_THRESHOLD) {
            this.windows.values().removeIf(existing -> !existing.isActive(now));
        }
        this.windows.merge(key, new Window(1, now.plus(this.window)),
                (existing, fresh) -> existing.isActive(now)
                        ? new Window(existing.attempts() + 1, existing.expiresAt())
                        : fresh);
    }

    @Override
    public void reset(final String key) {
        this.windows.remove(key);
    }

    private record Window(int attempts, Instant expiresAt) {

        boolean isActive(final Instant now) {
            return this.expiresAt.isAfter(now);
        }
    }
}
