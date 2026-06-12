package com.warehouse.commonassets.kafka.infrastructure.adapter.secondary;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxRecord;
import com.warehouse.commonassets.kafka.domain.port.KafkaOutboxPort;

@Repository
@ConditionalOnProperty(name = "manager.kafka.domain-events.enabled", havingValue = "true")
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
                            created_at
                        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
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
                Timestamp.from(Instant.now()));
    }

    @Override
    public List<KafkaOutboxRecord> findUnpublished(final int limit) {
        return this.jdbcTemplate.query("""
                        SELECT event_id,
                               topic,
                               message_key,
                               event_type,
                               event_version,
                               occurred_at,
                               operator_id,
                               payload_json,
                               headers_json
                          FROM kafka_event_outbox
                         WHERE published_at IS NULL
                         ORDER BY created_at
                         LIMIT ?
                        """,
                ps -> ps.setInt(1, limit),
                (rs, rowNum) -> toRecord(rs));
    }

    @Transactional
    @Override
    public void markPublished(final UUID eventId) {
        this.jdbcTemplate.update("""
                        UPDATE kafka_event_outbox
                           SET published_at = ?,
                               last_error = NULL
                         WHERE event_id = ?
                        """,
                Timestamp.from(Instant.now()),
                eventId.toString());
    }

    @Transactional
    @Override
    public void markFailed(final UUID eventId, final Throwable exception) {
        this.jdbcTemplate.update("""
                        UPDATE kafka_event_outbox
                           SET last_error = ?
                         WHERE event_id = ?
                        """,
                exception.getMessage(),
                eventId.toString());
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
                deserializeHeaders(rs.getString("headers_json"))
        );
    }

    private String serializeHeaders(final Map<String, String> headers) {
        try {
            return this.objectMapper.writeValueAsString(headers);
        } catch (final JsonProcessingException exception) {
            throw new IllegalStateException("Cannot serialize Kafka event headers", exception);
        }
    }

    private Map<String, String> deserializeHeaders(final String headers) {
        try {
            return this.objectMapper.readValue(headers, HEADERS_TYPE);
        } catch (final JsonProcessingException exception) {
            throw new IllegalStateException("Cannot deserialize Kafka event headers", exception);
        }
    }
}
