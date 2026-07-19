package com.warehouse.returning.infrastructure.adapter.secondary.outbox;

import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface ReturningOutboxRepository extends JpaRepository<ReturningOutboxEntity, UUID> {

    @Query(value = """
            SELECT * FROM returning_event_outbox
             WHERE (status = 'PENDING' AND (next_attempt_at IS NULL OR next_attempt_at <= :now))
                OR (status = 'PROCESSING' AND locked_until <= :now)
             ORDER BY created_at, event_id
             LIMIT 1 FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    Optional<ReturningOutboxEntity> findNext(@Param("now") final Instant now);

    @Modifying
    @Query("""
            UPDATE ReturningOutboxEntity e
               SET e.status = :status, e.publishedAt = :publishedAt, e.lastError = null,
                   e.nextAttemptAt = null, e.lockToken = null, e.lockedUntil = null
             WHERE e.eventId = :eventId AND e.lockToken = :lockToken
            """)
    int markPublished(@Param("eventId") final UUID eventId, @Param("lockToken") final UUID lockToken,
                      @Param("status") final KafkaOutboxStatus status, @Param("publishedAt") final Instant publishedAt);

    @Modifying
    @Query("""
            UPDATE ReturningOutboxEntity e
               SET e.status = :status, e.lastError = :lastError, e.nextAttemptAt = :nextAttemptAt,
                   e.lockToken = null, e.lockedUntil = null
             WHERE e.eventId = :eventId AND e.lockToken = :lockToken
            """)
    int markFailed(@Param("eventId") final UUID eventId, @Param("lockToken") final UUID lockToken,
                   @Param("status") final KafkaOutboxStatus status, @Param("lastError") final String lastError,
                   @Param("nextAttemptAt") final Instant nextAttemptAt);
}
