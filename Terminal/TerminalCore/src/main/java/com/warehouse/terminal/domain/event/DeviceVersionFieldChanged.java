package com.warehouse.terminal.domain.event;

import java.time.Instant;

import com.warehouse.terminal.domain.vo.DeviceSnapshot;

public class DeviceVersionFieldChanged extends DeviceChanged implements DeviceEvent {

    public DeviceVersionFieldChanged(final DeviceSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
