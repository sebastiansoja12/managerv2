package com.warehouse.terminal.domain.event;

import com.warehouse.terminal.domain.vo.DeviceSnapshot;

import java.time.Instant;

public class DeviceChanged implements DeviceEvent {
    private final DeviceSnapshot snapshot;
    private final Instant timestamp;

    public DeviceChanged(final DeviceSnapshot snapshot,
                         final Instant timestamp) {
        this.snapshot = snapshot;
        this.timestamp = timestamp;
    }

    public DeviceSnapshot getSnapshot() {
        return snapshot;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
