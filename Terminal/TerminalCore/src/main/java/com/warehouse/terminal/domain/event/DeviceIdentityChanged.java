package com.warehouse.terminal.domain.event;

import java.time.Instant;

import com.warehouse.terminal.domain.vo.DeviceSnapshot;

public class DeviceIdentityChanged extends DeviceChanged implements DeviceEvent {

    public DeviceIdentityChanged(final DeviceSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
