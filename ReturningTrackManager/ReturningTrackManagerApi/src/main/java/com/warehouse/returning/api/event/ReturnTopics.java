package com.warehouse.returning.api.event;

public final class ReturnTopics {

    public static final String PROCESSING_STARTED = "return.processing.started";
    public static final String CANCELLED = "return.cancelled";
    public static final String COMPLETED = "return.completed";
    public static final String READ_MODEL_CHANGED = "return.read-model.sync";

    private ReturnTopics() {
    }
}
