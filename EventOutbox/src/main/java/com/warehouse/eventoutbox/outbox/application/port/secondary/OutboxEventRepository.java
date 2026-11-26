package com.warehouse.eventoutbox.outbox.application.port.secondary;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.warehouse.eventoutbox.outbox.domain.enumeration.OutboxEventStatus;
import com.warehouse.eventoutbox.outbox.domain.model.OutboxEvent;

public interface OutboxEventRepository {

    void save(final OutboxEvent outboxEvent);

    void saveAll(final List<OutboxEvent> outboxEvents);

    List<OutboxEvent> findPublishable(final Instant now, final int limit);

    int deletePublishedBefore(final Instant publishedBefore, final int limit);

    long countByStatus(final OutboxEventStatus status);

    Optional<Instant> findOldestPendingOccurredAt();
}
