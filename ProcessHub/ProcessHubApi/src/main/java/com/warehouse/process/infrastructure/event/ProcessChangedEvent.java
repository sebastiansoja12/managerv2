package com.warehouse.process.infrastructure.event;

import com.warehouse.process.infrastructure.dto.ProcessLogId;

public class ProcessChangedEvent implements ProcessLogEvent {
    private final ProcessLogId processLogId;

    public ProcessChangedEvent(final ProcessLogId processLogId) {
        this.processLogId = processLogId;
    }
}
