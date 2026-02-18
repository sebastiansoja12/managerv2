package com.warehouse.process.domain.event;

import java.time.Instant;

import com.warehouse.commonassets.identificator.ProcessId;

public class ProcessChangedEvent implements ProcessEvent {

    private final ProcessId processId;
    private final String operation;
    private final Instant occurredAt;

    public ProcessChangedEvent(final ProcessId processId, final String operation, final Instant occurredAt) {
        this.processId = processId;
        this.operation = operation;
        this.occurredAt = occurredAt;
    }

    public ProcessId getProcessId() {
        return processId;
    }

    public String getOperation() {
        return operation;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}
