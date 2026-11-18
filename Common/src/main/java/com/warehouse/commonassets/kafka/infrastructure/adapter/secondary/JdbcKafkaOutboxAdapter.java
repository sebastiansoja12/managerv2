package com.warehouse.commonassets.kafka.infrastructure.adapter.secondary;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxRecord;
import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxStatus;
import com.warehouse.commonassets.kafka.domain.port.KafkaOutboxPort;

@Repository
@ConditionalOnProperty(name = "manager.kafka.outbox.enabled", havingValue = "true")
public class JdbcKafkaOutboxAdapter implements KafkaOutboxPort {

    private static final TypeReference<Map<String, String>> HEADERS_TYPE = new TypeReference<>() {
    };

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public JdbcKafkaOutboxAdapter(final JdbcTemplate jdbcTemplate, final ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    @Transactional
    @Override
    public void save(final KafkaOutboxRecord record) {
        this.jdbcTemplate.update("""
                        INSERT INTO kafka_event_outbox (
                            event_id,
                            topic,
                            message_key,
                            event_type,
                            event_version,
                            occurred_at,
                            operator_id,
                            payload_json,
                            headers_json,
                            created_at,
                            status,
                            attempt_count
                        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                        """,
                record.eventId().toString(),
                record.topic(),
                record.messageKey(),
                record.eventType(),
                record.eventVersion(),
                Timestamp.from(record.occurredAt()),
                record.operatorId() == null ? null : record.operatorId().getValue(),
                record.payload(),
                serializeHeaders(record.headers()),
                Timestamp.from(Instant.now()),
                record.status().name(),
                record.attemptCount());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public List<KafkaOutboxRecord> claimPending(final int limit,
                                                final String workerId,
                                                final Instant now,
                                                final Instant lockedUntil) {
        final List<UUID> candidates = this.jdbcTemplate.query("""
                        SELECT event_id
                          FROM kafka_event_outbox
                         WHERE (status IN (?, ?) AND (next_attempt_at IS NULL OR next_attempt_at <= ?))
                            OR (status = ? AND locked_until <= ?)
                         ORDER BY created_at
                         LIMIT ?
                        """,
                ps -> {
                    ps.setString(1, KafkaOutboxStatus.PENDING.name());
                    ps.setString(2, KafkaOutboxStatus.DEAD.name());
                    ps.setTimestamp(3, Timestamp.from(now));
                    ps.setString(4, KafkaOutboxStatus.PROCESSING.name());
                    ps.setTimestamp(5, Timestamp.from(now));
                    ps.setInt(6, limit);
                },
                (rs, rowNum) -> UUID.fromString(rs.getString("event_id")));

        final List<KafkaOutboxRecord> claimed = new ArrayList<>();
        candidates.forEach(eventId -> {
            if (claim(eventId, workerId, now, lockedUntil)) {
                claimed.add(findById(eventId));
            }
        });
        return claimed;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public boolean claim(final UUID eventId,
                         final String workerId,
                         final Instant now,
                         final Instant lockedUntil) {
        return this.jdbcTemplate.update("""
                        UPDATE kafka_event_outbox
                           SET status = ?,
                               locked_by = ?,
                               locked_until = ?
                         WHERE event_id = ?
                           AND ((status IN (?, ?) AND (next_attempt_at IS NULL OR next_attempt_at <= ?))
                             OR (status = ? AND locked_until <= ?))
                        """,
                KafkaOutboxStatus.PROCESSING.name(),
                workerId,
                Timestamp.from(lockedUntil),
                eventId.toString(),
                KafkaOutboxStatus.PENDING.name(),
                KafkaOutboxStatus.DEAD.name(),
                Timestamp.from(now),
                KafkaOutboxStatus.PROCESSING.name(),
                Timestamp.from(now)) == 1;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void markPublished(final UUID eventId, final String workerId) {
        this.jdbcTemplate.update("""
                        UPDATE kafka_event_outbox
                           SET status = ?,
                               published_at = ?,
                               last_error = NULL,
                               next_attempt_at = NULL,
                               locked_by = NULL,
                               locked_until = NULL
                         WHERE event_id = ?
                           AND status = ?
                           AND locked_by = ?
                        """,
                KafkaOutboxStatus.PUBLISHED.name(),
                Timestamp.from(Instant.now()),
                eventId.toString(),
                KafkaOutboxStatus.PROCESSING.name(),
                workerId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void markFailed(final UUID eventId,
                           final String workerId,
                           final Throwable exception,
                           final Instant nextAttemptAt) {
        this.jdbcTemplate.update("""
                        UPDATE kafka_event_outbox
                           SET status = ?,
                               next_attempt_at = ?,
                               attempt_count = attempt_count + 1,
                               last_error = ?,
                               locked_by = NULL,
                               locked_until = NULL
                         WHERE event_id = ?
                           AND status = ?
                           AND locked_by = ?
                        """,
                KafkaOutboxStatus.PENDING.name(),
                Timestamp.from(nextAttemptAt),
                errorMessage(exception),
                eventId.toString(),
                KafkaOutboxStatus.PROCESSING.name(),
                workerId);
    }

    private KafkaOutboxRecord findById(final UUID eventId) {
        return this.jdbcTemplate.query("""
                        SELECT event_id,
                               topic,
                               message_key,
                               event_type,
                               event_version,
                               occurred_at,
                               operator_id,
                               payload_json,
                               headers_json,
                               status,
                               attempt_count
                          FROM kafka_event_outbox
                         WHERE event_id = ?
                        """,
                ps -> ps.setString(1, eventId.toString()),
                (rs, rowNum) -> toRecord(rs))
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Claimed Kafka outbox record does not exist: " + eventId));
    }

    private KafkaOutboxRecord toRecord(final ResultSet rs) throws SQLException {
        final Long operatorId = rs.getObject("operator_id", Long.class);
        return new KafkaOutboxRecord(
                UUID.fromString(rs.getString("event_id")),
                rs.getString("topic"),
                rs.getString("message_key"),
                rs.getString("event_type"),
                rs.getInt("event_version"),
                rs.getTimestamp("occurred_at").toInstant(),
                operatorId == null ? null : OperatorId.of(operatorId),
                rs.getString("payload_json"),
                deserializeHeaders(rs.getString("headers_json")),
                KafkaOutboxStatus.valueOf(rs.getString("status")),
                rs.getInt("attempt_count")
        );
    }

    private String errorMessage(final Throwable exception) {
        if (exception.getMessage() != null) {
            return exception.getMessage();
        }
        return exception.getClass().getName();
    }

    private String serializeHeaders(final Map<String, String> headers) {
        try {
            return this.objectMapper.writeValueAsString(headers);
        } catch (final JacksonException exception) {
            throw new IllegalStateException("Cannot serialize Kafka event headers", exception);
        }
    }

    private Map<String, String> deserializeHeaders(final String headers) {
        try {
            return this.objectMapper.readValue(headers, HEADERS_TYPE);
        } catch (final JacksonException exception) {
            throw new IllegalStateException("Cannot deserialize Kafka event headers", exception);
        }
    }
}
