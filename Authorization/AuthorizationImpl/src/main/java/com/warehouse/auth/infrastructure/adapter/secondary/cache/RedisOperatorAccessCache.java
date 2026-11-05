package com.warehouse.auth.infrastructure.adapter.secondary.cache;

import com.warehouse.commonassets.identificator.OperatorId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
public class RedisOperatorAccessCache implements OperatorAccessCache {

    static final Duration SESSION_REVOCATION_TTL = Duration.ofMinutes(1);

    static final Duration OPERATOR_STATUS_TTL = Duration.ofSeconds(15);

    static final String NOT_REVOKED = "none";

    private final StringRedisTemplate redisTemplate;

    public RedisOperatorAccessCache(final StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Optional<CachedSessionRevocation> findSessionRevocation(final OperatorId operatorId) {
        return read(() -> Optional.ofNullable(redisTemplate.opsForValue().get(sessionRevocationKey(operatorId)))
                .map(value -> new CachedSessionRevocation(NOT_REVOKED.equals(value)
                        ? null
                        : Instant.ofEpochMilli(Long.parseLong(value)))));
    }

    @Override
    public void putSessionRevocation(final OperatorId operatorId, final CachedSessionRevocation revocation) {
        final String value = revocation.asOptional()
                .map(revokedAt -> String.valueOf(revokedAt.toEpochMilli()))
                .orElse(NOT_REVOKED);
        write(() -> redisTemplate.opsForValue().set(sessionRevocationKey(operatorId), value, SESSION_REVOCATION_TTL));
    }

    @Override
    public Optional<Boolean> findOperatorActive(final OperatorId operatorId) {
        return read(() -> Optional.ofNullable(redisTemplate.opsForValue().get(operatorStatusKey(operatorId)))
                .map(Boolean::parseBoolean));
    }

    @Override
    public void putOperatorActive(final OperatorId operatorId, final boolean active) {
        write(() -> redisTemplate.opsForValue().set(operatorStatusKey(operatorId), String.valueOf(active),
                OPERATOR_STATUS_TTL));
    }

    static String sessionRevocationKey(final OperatorId operatorId) {
        return "auth:operator:" + operatorId.value() + ":sessions-revoked-at";
    }

    static String operatorStatusKey(final OperatorId operatorId) {
        return "auth:operator:" + operatorId.value() + ":active";
    }

    private <T> Optional<T> read(final Supplier<Optional<T>> operation) {
        try {
            return operation.get();
        } catch (final DataAccessException | NumberFormatException exception) {
            log.warn("Redis operator access cache read failed, falling back to database: {}", exception.getMessage());
            return Optional.empty();
        }
    }

    private void write(final Runnable operation) {
        try {
            operation.run();
        } catch (final DataAccessException exception) {
            log.warn("Redis operator access cache write failed: {}", exception.getMessage());
        }
    }
}
