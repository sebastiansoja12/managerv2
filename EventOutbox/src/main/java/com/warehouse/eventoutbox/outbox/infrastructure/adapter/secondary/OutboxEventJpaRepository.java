package com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.warehouse.eventoutbox.outbox.domain.enumeration.OutboxEventStatus;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.entity.OutboxEventEntity;
import com.warehouse.eventoutbox.integration.model.EventId;

public interface OutboxEventJpaRepository extends JpaRepository<OutboxEventEntity, Long> {

    @Query("""
            SELECT candidate FROM OutboxEventEntity candidate
             WHERE candidate.status = :pending
               AND candidate.nextAttemptAt <= :now
               AND NOT EXISTS (
                   SELECT earlier.sequenceNumber FROM OutboxEventEntity earlier
                    WHERE earlier.messageKey = candidate.messageKey
                      AND earlier.sequenceNumber < candidate.sequenceNumber
                      AND (earlier.status = :dead OR (earlier.status = :pending AND earlier.nextAttemptAt > :now)))
             ORDER BY candidate.sequenceNumber
            """)
    List<OutboxEventEntity> findPublishable(@Param("pending") final OutboxEventStatus pending,
                                            @Param("dead") final OutboxEventStatus dead,
                                            @Param("now") final Instant now,
                                            final Pageable pageable);

    List<OutboxEventEntity> findAllByEventIdIn(final Collection<EventId> eventIds);

    @Query("""
            SELECT outboxEvent.sequenceNumber FROM OutboxEventEntity outboxEvent
             WHERE outboxEvent.status = :status
               AND outboxEvent.publishedAt < :publishedBefore
             ORDER BY outboxEvent.sequenceNumber
            """)
    List<Long> findSequenceNumbersPublishedBefore(@Param("status") final OutboxEventStatus status,
                                                  @Param("publishedBefore") final Instant publishedBefore,
                                                  final Pageable pageable);

    long countByStatus(final OutboxEventStatus status);

    @Query("SELECT MIN(outboxEvent.occurredAt) FROM OutboxEventEntity outboxEvent WHERE outboxEvent.status = :status")
    Instant findOldestOccurredAt(@Param("status") final OutboxEventStatus status);
}
