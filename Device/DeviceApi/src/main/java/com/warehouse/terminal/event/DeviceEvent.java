package com.warehouse.terminal.event;

import java.time.Instant;

public class DeviceEvent {

    private final Instant timestamp;

    public DeviceEvent(final Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
