package com.warehouse.commonassets.kafka.domain.model;

public final class KafkaEventHeaders {

    public static final String EVENT_ID = "eventId";
    public static final String EVENT_TYPE = "eventType";
    public static final String EVENT_VERSION = "eventVersion";
    public static final String OCCURRED_AT = "occurredAt";
    public static final String OPERATOR_ID = "operatorId";
    public static final String CORRELATION_ID = "correlationId";
    public static final String CAUSATION_ID = "causationId";

    private KafkaEventHeaders() {
    }
}
