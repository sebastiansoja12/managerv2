package com.warehouse.eventoutbox.outbox.application.port.secondary;

import com.warehouse.eventoutbox.outbox.domain.model.OutboxEvent;

@FunctionalInterface
public interface OutboxDeadLetterPort {

    OutboxDeadLetterPort NONE = event -> {
    };

    void publish(OutboxEvent deadEvent);
}
