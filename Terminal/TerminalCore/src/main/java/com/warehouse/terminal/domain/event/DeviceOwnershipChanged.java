package com.warehouse.terminal.domain.event;

import java.time.Instant;

import com.warehouse.terminal.domain.vo.DeviceSnapshot;

public class DeviceOwnershipChanged extends DeviceChanged implements DeviceEvent {

    public DeviceOwnershipChanged(final DeviceSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
