package com.warehouse.returning.infrastructure.adapter.secondary;

import com.warehouse.returning.application.port.secondary.ReturnConsumedEventServicePort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import com.warehouse.returning.domain.vo.ReturnPackageId;

@Component
public class ReturnConsumedEventServiceAdapter implements ReturnConsumedEventServicePort {

    private final JdbcTemplate jdbcTemplate;

    public ReturnConsumedEventServiceAdapter(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean tryConsume(final UUID eventId) {
        return jdbcTemplate.update("""
                INSERT INTO return_consumed_event (event_id, consumed_at)
                VALUES (?, ?)
                ON CONFLICT (event_id) DO NOTHING
                """, eventId, Timestamp.from(Instant.now())) == 1;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean lockAndCheckCancelled(final ReturnPackageId returnPackageId) {
        jdbcTemplate.update("""
                INSERT INTO return_processing_state (return_package_id, cancelled) VALUES (?, false)
                ON CONFLICT (return_package_id) DO NOTHING
                """, returnPackageId.value());
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("""
                SELECT cancelled FROM return_processing_state WHERE return_package_id = ? FOR UPDATE
                """, Boolean.class, returnPackageId.value()));
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void markCancelled(final ReturnPackageId returnPackageId) {
        jdbcTemplate.update("UPDATE return_processing_state SET cancelled = true WHERE return_package_id = ?",
                returnPackageId.value());
    }
}
