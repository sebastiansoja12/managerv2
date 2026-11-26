package com.warehouse.billing.payment.domain.service;

import java.time.Duration;
import java.time.Instant;

public class CheckoutExpiryPolicy {

    private static final Duration MAXIMUM_LIFETIME = Duration.ofHours(24);

    private static final Duration MINIMUM_LIFETIME = Duration.ofMinutes(30);

    public Instant expiresAt(final Instant deadline, final Instant now) {
        final Instant latest = now.plus(MAXIMUM_LIFETIME);
        final Instant earliest = now.plus(MINIMUM_LIFETIME);
        final Instant capped = deadline.isBefore(latest) ? deadline : latest;
        return capped.isBefore(earliest) ? earliest : capped;
    }
}
