package com.warehouse.terminal.domain.event;

import java.time.Instant;

import com.warehouse.terminal.domain.vo.DeviceSnapshot;

public class DeviceStatusChanged extends DeviceChanged implements DeviceEvent {

    public DeviceStatusChanged(final DeviceSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
