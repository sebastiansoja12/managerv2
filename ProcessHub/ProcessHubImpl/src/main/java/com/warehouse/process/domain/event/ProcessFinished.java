package com.warehouse.process.domain.event;

import com.warehouse.process.domain.vo.ProcessLogSnapshot;

import java.time.Instant;

public class ProcessFinished extends ProcessChangedEvent{
    public ProcessFinished(final ProcessLogSnapshot snapshot, final Instant occurredAt) {
        super(snapshot, occurredAt);
    }
}
