package com.warehouse.returning.infrastructure.adapter.secondary.outbox;

public enum ReturningOutboxStatus {
    PENDING,
    PROCESSING,
    PUBLISHED,
    DEAD
}
