package com.warehouse.commonassets.kafka.domain.model;

public enum KafkaOutboxStatus {
    PENDING,
    PROCESSING,
    PUBLISHED,
    DEAD
}
