package com.warehouse.commonassets.exception;

import java.time.Instant;

import com.warehouse.commonassets.identificator.ProcessId;

public final class ProcessFailureDetails {

    private final ProcessId processId;
    private final String sourceService;
    private final String targetService;
    private final String exceptionMessage;
    private final Instant occurredAt;

    public ProcessFailureDetails(
            final ProcessId processId,
            final String sourceService,
            final String targetService,
            final String exceptionMessage,
            final Instant occurredAt
    ) {
        this.processId = processId;
        this.sourceService = sourceService;
        this.targetService = targetService;
        this.exceptionMessage = exceptionMessage;
        this.occurredAt = occurredAt;
    }

    public static ProcessFailureDetails now(
            final ProcessId processId,
            final String sourceService,
            final String targetService,
            final String exceptionMessage
    ) {
        return new ProcessFailureDetails(
                processId,
                sourceService,
                targetService,
                exceptionMessage,
                Instant.now()
        );
    }

    public ProcessId getProcessId() {
        return processId;
    }

    public String getSourceService() {
        return sourceService;
    }

    public String getTargetService() {
        return targetService;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}

