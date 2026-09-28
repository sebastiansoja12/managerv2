package com.warehouse.auth.infrastructure.adapter.secondary.cache;

import com.warehouse.commonassets.identificator.OperatorId;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOperatorAccessCache implements OperatorAccessCache {

    private static final Duration CACHE_TTL = Duration.ofSeconds(15);

    private final Clock clock;

    private final Map<Long, Expiring<CachedSessionRevocation>> revocations = new ConcurrentHashMap<>();

    private final Map<Long, Expiring<Boolean>> statuses = new ConcurrentHashMap<>();

    public InMemoryOperatorAccessCache(final Clock clock) {
        this.clock = clock;
    }

    @Override
    public Optional<CachedSessionRevocation> findSessionRevocation(final OperatorId operatorId) {
        return find(revocations, operatorId);
    }

    @Override
    public void putSessionRevocation(final OperatorId operatorId, final CachedSessionRevocation revocation) {
        revocations.put(operatorId.value(), new Expiring<>(revocation, Instant.now(clock).plus(CACHE_TTL)));
    }

    @Override
    public Optional<Boolean> findOperatorActive(final OperatorId operatorId) {
        return find(statuses, operatorId);
    }

    @Override
    public void putOperatorActive(final OperatorId operatorId, final boolean active) {
        statuses.put(operatorId.value(), new Expiring<>(active, Instant.now(clock).plus(CACHE_TTL)));
    }

    private <T> Optional<T> find(final Map<Long, Expiring<T>> entries, final OperatorId operatorId) {
        final Expiring<T> entry = entries.get(operatorId.value());
        if (entry == null || !entry.expiresAt().isAfter(Instant.now(clock))) {
            return Optional.empty();
        }
        return Optional.of(entry.value());
    }

    private record Expiring<T>(T value, Instant expiresAt) {
    }
}
