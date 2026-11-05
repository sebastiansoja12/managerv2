package com.warehouse.auth.infrastructure.adapter.secondary.cache;

import com.warehouse.auth.infrastructure.adapter.secondary.cache.OperatorAccessCache.CachedSessionRevocation;
import com.warehouse.commonassets.identificator.OperatorId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedisOperatorAccessCacheTest {

    private static final OperatorId OPERATOR_ID = OperatorId.of(7L);
    private static final Instant REVOKED_AT = Instant.parse("2026-11-02T08:00:00Z");

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private RedisOperatorAccessCache cache;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        cache = new RedisOperatorAccessCache(redisTemplate);
    }

    @Test
    void shouldStoreSessionRevocationAsEpochMillisWithTtl() {
        cache.putSessionRevocation(OPERATOR_ID, new CachedSessionRevocation(REVOKED_AT));

        verify(valueOperations).set("auth:operator:7:sessions-revoked-at", String.valueOf(REVOKED_AT.toEpochMilli()),
                RedisOperatorAccessCache.SESSION_REVOCATION_TTL);
    }

    @Test
    void shouldReadSessionRevocation() {
        when(valueOperations.get("auth:operator:7:sessions-revoked-at"))
                .thenReturn(String.valueOf(REVOKED_AT.toEpochMilli()));

        assertThat(cache.findSessionRevocation(OPERATOR_ID))
                .contains(new CachedSessionRevocation(REVOKED_AT));
    }

    @Test
    void shouldCacheMissingRevocationAsNotRevoked() {
        cache.putSessionRevocation(OPERATOR_ID, CachedSessionRevocation.of(Optional.empty()));
        when(valueOperations.get("auth:operator:7:sessions-revoked-at"))
                .thenReturn(RedisOperatorAccessCache.NOT_REVOKED);

        verify(valueOperations).set("auth:operator:7:sessions-revoked-at", RedisOperatorAccessCache.NOT_REVOKED,
                RedisOperatorAccessCache.SESSION_REVOCATION_TTL);
        assertThat(cache.findSessionRevocation(OPERATOR_ID))
                .hasValueSatisfying(revocation -> assertThat(revocation.asOptional()).isEmpty());
    }

    @Test
    void shouldStoreAndReadOperatorStatus() {
        cache.putOperatorActive(OPERATOR_ID, false);
        when(valueOperations.get("auth:operator:7:active")).thenReturn("false");

        verify(valueOperations).set("auth:operator:7:active", "false", RedisOperatorAccessCache.OPERATOR_STATUS_TTL);
        assertThat(cache.findOperatorActive(OPERATOR_ID)).contains(false);
    }

    @Test
    void shouldFallBackToMissWhenRedisIsUnavailable() {
        when(valueOperations.get(anyString())).thenThrow(new RedisConnectionFailureException("down"));

        assertThat(cache.findSessionRevocation(OPERATOR_ID)).isEmpty();
        assertThat(cache.findOperatorActive(OPERATOR_ID)).isEmpty();
    }

    @Test
    void shouldIgnoreWriteFailures() {
        doThrow(new RedisConnectionFailureException("down"))
                .when(valueOperations).set(anyString(), anyString(), any(Duration.class));

        cache.putOperatorActive(OPERATOR_ID, true);
    }
}
