package com.warehouse.eventoutbox.outbox.application.port.secondary;

import com.warehouse.eventoutbox.outbox.domain.model.OutboxEvent;

/**
 * Makes events that exhausted their retries visible outside the service database, so dead events of every service
 * end up on one topic that can be journaled and alerted on.
 */
@FunctionalInterface
public interface OutboxDeadLetterPort {

    OutboxDeadLetterPort NONE = event -> {
    };

    /**
     * Best effort: failures are logged and never affect the relay, the event stays DEAD in the outbox table.
     */
    void publish(OutboxEvent deadEvent);
}
