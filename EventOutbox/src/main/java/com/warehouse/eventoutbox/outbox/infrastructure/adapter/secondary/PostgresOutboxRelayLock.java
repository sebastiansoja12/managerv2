package com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary;

import org.springframework.jdbc.core.JdbcTemplate;

import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxRelayLock;

public class PostgresOutboxRelayLock implements OutboxRelayLock {

    private final JdbcTemplate jdbcTemplate;
    private final long lockKey;

    public PostgresOutboxRelayLock(final JdbcTemplate jdbcTemplate, final long lockKey) {
        this.jdbcTemplate = jdbcTemplate;
        this.lockKey = lockKey;
    }

    @Override
    public boolean tryAcquire() {
        return Boolean.TRUE.equals(this.jdbcTemplate.queryForObject(
                "SELECT pg_try_advisory_xact_lock(?)", Boolean.class, this.lockKey));
    }
}
