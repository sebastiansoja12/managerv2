package com.warehouse.shipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxRecord;
import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxStatus;
import com.warehouse.commonassets.kafka.infrastructure.adapter.secondary.JdbcKafkaOutboxAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcKafkaOutboxAdapterTest {

    private EmbeddedDatabase database;
    private JdbcTemplate jdbcTemplate;
    private JdbcKafkaOutboxAdapter adapter;

    @BeforeEach
    void setUp() {
        this.database = new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.HSQL)
                .build();
        this.jdbcTemplate = new JdbcTemplate(this.database);
        this.jdbcTemplate.execute("""
                CREATE TABLE kafka_event_outbox (
                    event_id VARCHAR(36) PRIMARY KEY,
                    topic VARCHAR(255) NOT NULL,
                    message_key VARCHAR(255),
                    event_type VARCHAR(512) NOT NULL,
                    event_version INT NOT NULL,
                    occurred_at TIMESTAMP NOT NULL,
                    operator_id BIGINT,
                    payload_json CLOB NOT NULL,
                    headers_json CLOB NOT NULL,
                    created_at TIMESTAMP NOT NULL,
                    published_at TIMESTAMP,
                    last_error CLOB,
                    status VARCHAR(32) NOT NULL,
                    attempt_count INT NOT NULL,
                    next_attempt_at TIMESTAMP,
                    locked_by VARCHAR(64),
                    locked_until TIMESTAMP
                )
                """);
        this.adapter = new JdbcKafkaOutboxAdapter(
                this.jdbcTemplate, new ObjectMapper().findAndRegisterModules());
    }

    @AfterEach
    void tearDown() {
        this.database.shutdown();
    }

    @Test
    void shouldAllowOnlyOneWorkerToClaimRecord() {
        final KafkaOutboxRecord record = record();
        final Instant now = Instant.parse("2026-08-31T12:00:00Z");
        this.adapter.save(record);

        final List<KafkaOutboxRecord> firstClaim =
                this.adapter.claimPending(10, "worker-1", now, now.plusSeconds(30));
        final boolean secondClaim =
                this.adapter.claim(record.eventId(), "worker-2", now, now.plusSeconds(30));

        assertThat(firstClaim).hasSize(1);
        assertThat(firstClaim.getFirst().eventId()).isEqualTo(record.eventId());
        assertThat(firstClaim.getFirst().status()).isEqualTo(KafkaOutboxStatus.PROCESSING);
        assertThat(secondClaim).isFalse();
    }

    @Test
    void shouldAtomicallyClaimRecordWhenWorkersRace() throws Exception {
        final KafkaOutboxRecord record = record();
        final Instant now = Instant.parse("2026-08-31T12:00:00Z");
        final CountDownLatch start = new CountDownLatch(1);
        final ExecutorService workers = Executors.newFixedThreadPool(2);
        this.adapter.save(record);

        try {
            final Future<Boolean> first = workers.submit(() -> {
                start.await();
                return this.adapter.claim(record.eventId(), "worker-1", now, now.plusSeconds(30));
            });
            final Future<Boolean> second = workers.submit(() -> {
                start.await();
                return this.adapter.claim(record.eventId(), "worker-2", now, now.plusSeconds(30));
            });

            start.countDown();

            assertThat(List.of(first.get(), second.get())).containsExactlyInAnyOrder(true, false);
        } finally {
            workers.shutdownNow();
        }
    }

    @Test
    void shouldReleaseFailedRecordForRetryAndMoveItToDeadAfterMaxAttempts() {
        final KafkaOutboxRecord record = record();
        final Instant now = Instant.parse("2026-08-31T12:00:00Z");
        final Instant nextAttemptAt = now.plusSeconds(5);
        this.adapter.save(record);
        this.adapter.claim(record.eventId(), "worker-1", now, now.plusSeconds(30));

        this.adapter.markFailed(
                record.eventId(), "worker-1", new RuntimeException("Kafka unavailable"), nextAttemptAt, 2);

        assertThat(status(record.eventId())).isEqualTo(KafkaOutboxStatus.PENDING);
        assertThat(attemptCount(record.eventId())).isEqualTo(1);
        assertThat(this.adapter.claim(record.eventId(), "worker-2", now.plusSeconds(1), now.plusSeconds(31)))
                .isFalse();
        assertThat(this.adapter.claim(record.eventId(), "worker-2", nextAttemptAt, nextAttemptAt.plusSeconds(30)))
                .isTrue();

        this.adapter.markFailed(
                record.eventId(), "worker-2", new RuntimeException("Still unavailable"),
                nextAttemptAt.plusSeconds(5), 2);

        assertThat(status(record.eventId())).isEqualTo(KafkaOutboxStatus.DEAD);
        assertThat(attemptCount(record.eventId())).isEqualTo(2);
        assertThat(this.adapter.claim(
                record.eventId(), "worker-3", nextAttemptAt.plusSeconds(60), nextAttemptAt.plusSeconds(90)))
                .isFalse();
    }

    @Test
    void shouldRecoverProcessingRecordAfterLockExpires() {
        final KafkaOutboxRecord record = record();
        final Instant now = Instant.parse("2026-08-31T12:00:00Z");
        this.adapter.save(record);
        this.adapter.claim(record.eventId(), "worker-1", now, now.plusSeconds(30));

        assertThat(this.adapter.claim(record.eventId(), "worker-2", now.plusSeconds(29), now.plusSeconds(59)))
                .isFalse();
        assertThat(this.adapter.claim(record.eventId(), "worker-2", now.plusSeconds(31), now.plusSeconds(61)))
                .isTrue();
    }

    @Test
    void shouldMarkOnlyRecordOwnedByWorkerAsPublished() {
        final KafkaOutboxRecord record = record();
        final Instant now = Instant.parse("2026-08-31T12:00:00Z");
        this.adapter.save(record);
        this.adapter.claim(record.eventId(), "worker-1", now, now.plusSeconds(30));

        this.adapter.markPublished(record.eventId(), "worker-2");
        assertThat(status(record.eventId())).isEqualTo(KafkaOutboxStatus.PROCESSING);

        this.adapter.markPublished(record.eventId(), "worker-1");
        assertThat(status(record.eventId())).isEqualTo(KafkaOutboxStatus.PUBLISHED);
        assertThat(this.jdbcTemplate.queryForObject(
                "SELECT published_at FROM kafka_event_outbox WHERE event_id = ?",
                Timestamp.class,
                record.eventId().toString())).isNotNull();
    }

    private KafkaOutboxStatus status(final UUID eventId) {
        return KafkaOutboxStatus.valueOf(this.jdbcTemplate.queryForObject(
                "SELECT status FROM kafka_event_outbox WHERE event_id = ?",
                String.class,
                eventId.toString()));
    }

    private Integer attemptCount(final UUID eventId) {
        return this.jdbcTemplate.queryForObject(
                "SELECT attempt_count FROM kafka_event_outbox WHERE event_id = ?",
                Integer.class,
                eventId.toString());
    }

    private KafkaOutboxRecord record() {
        return new KafkaOutboxRecord(
                UUID.randomUUID(),
                "shipment.read-model.sync",
                "shipment-1",
                "shipment.read-model.changed",
                1,
                Instant.parse("2026-08-31T11:59:00Z"),
                null,
                "{}",
                Map.of("__TypeId__", "ShipmentReadModelChanged")
        );
    }
}
