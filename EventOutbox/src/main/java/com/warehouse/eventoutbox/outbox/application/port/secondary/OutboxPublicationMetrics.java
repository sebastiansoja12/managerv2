package com.warehouse.eventoutbox.outbox.application.port.secondary;

import com.warehouse.eventoutbox.outbox.domain.model.OutboxEvent;

public interface OutboxPublicationMetrics {

    void published(final OutboxEvent outboxEvent);

    void retryScheduled(final OutboxEvent outboxEvent);

    void dead(final OutboxEvent outboxEvent);
}
