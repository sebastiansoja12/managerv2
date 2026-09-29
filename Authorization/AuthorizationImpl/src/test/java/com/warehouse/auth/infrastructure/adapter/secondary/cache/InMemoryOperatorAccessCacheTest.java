package com.warehouse.auth.infrastructure.adapter.secondary.cache;

import com.warehouse.auth.infrastructure.adapter.secondary.cache.OperatorAccessCache.CachedSessionRevocation;
import com.warehouse.commonassets.identificator.OperatorId;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryOperatorAccessCacheTest {

    private static final OperatorId OPERATOR_ID = OperatorId.of(7L);
    private static final Instant NOW = Instant.parse("2026-11-02T08:00:00Z");

    private final AtomicReference<Instant> now = new AtomicReference<>(NOW);
    private final InMemoryOperatorAccessCache cache = new InMemoryOperatorAccessCache(new Clock() {
        @Override
        public ZoneOffset getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(final ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return now.get();
        }
    });

    @Test
    void shouldReturnCachedEntriesUntilTheyExpire() {
        cache.putSessionRevocation(OPERATOR_ID, new CachedSessionRevocation(NOW));
        cache.putOperatorActive(OPERATOR_ID, true);

        assertThat(cache.findSessionRevocation(OPERATOR_ID)).contains(new CachedSessionRevocation(NOW));
        assertThat(cache.findOperatorActive(OPERATOR_ID)).contains(true);

        now.set(NOW.plusSeconds(15));

        assertThat(cache.findSessionRevocation(OPERATOR_ID)).isEmpty();
        assertThat(cache.findOperatorActive(OPERATOR_ID)).isEmpty();
    }
}
