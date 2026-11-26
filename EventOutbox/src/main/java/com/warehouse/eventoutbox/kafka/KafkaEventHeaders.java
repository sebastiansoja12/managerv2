package com.warehouse.eventoutbox.kafka;

public final class KafkaEventHeaders {

    public static final String TYPE_ID = "__TypeId__";
    public static final String EVENT_ID = "eventId";
    public static final String EVENT_TYPE = "eventType";
    public static final String EVENT_CLASS = "eventClass";
    public static final String EVENT_VERSION = "eventVersion";
    public static final String OCCURRED_AT = "occurredAt";
    public static final String OPERATOR_ID = "operatorId";
    public static final String USER_ID = "userId";
    public static final String DEPARTMENT_ID = "departmentId";
    public static final String CORRELATION_ID = "correlationId";
    public static final String CAUSATION_ID = "causationId";
    public static final String CE_SPEC_VERSION = "ce_specversion";
    public static final String CE_ID = "ce_id";
    public static final String CE_TYPE = "ce_type";
    public static final String CE_SOURCE = "ce_source";
    public static final String CE_TIME = "ce_time";
    public static final String CE_SUBJECT = "ce_subject";
    public static final String CLOUD_EVENTS_SPEC_VERSION = "1.0";
    public static final String TRACEPARENT = "traceparent";
    public static final String TRACESTATE = "tracestate";
    public static final String OUTBOX_ORIGINAL_TOPIC = "outbox_original_topic";
    public static final String OUTBOX_ORIGINAL_KEY = "outbox_original_key";
    public static final String OUTBOX_ATTEMPTS = "outbox_attempts";
    public static final String OUTBOX_ERROR = "outbox_error";

    private KafkaEventHeaders() {
    }
}
