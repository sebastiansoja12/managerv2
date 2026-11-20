package com.warehouse.eventoutbox.outbox.application.port.secondary;

import java.util.concurrent.CompletableFuture;

import com.warehouse.eventoutbox.outbox.domain.model.OutboxEvent;

public interface OutboxEventSenderServicePort {

    CompletableFuture<Void> send(final OutboxEvent outboxEvent);
}
