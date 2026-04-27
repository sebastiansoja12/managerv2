package com.warehouse.terminal.domain.event;

import java.time.Instant;

import com.warehouse.terminal.domain.vo.DeviceSnapshot;

public class DeviceLocationChanged extends DeviceChanged implements DeviceEvent {

    public DeviceLocationChanged(final DeviceSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
