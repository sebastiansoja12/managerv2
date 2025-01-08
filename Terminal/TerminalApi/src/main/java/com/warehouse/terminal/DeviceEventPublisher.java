package com.warehouse.terminal;

import com.warehouse.terminal.event.DeviceLogEvent;

public interface DeviceEventPublisher {
    void send(final DeviceLogEvent deviceLogEvent);
}
