package com.warehouse.returning.infrastructure.adapter.secondary.outbox;

import java.util.UUID;

public record ReturningOutboxMessage(UUID eventId, String topic, String messageKey, String payload,
                                     String headers, int attemptCount, UUID lockToken) {
}
