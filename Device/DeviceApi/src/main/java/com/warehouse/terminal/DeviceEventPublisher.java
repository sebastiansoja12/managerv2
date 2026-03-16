package com.warehouse.terminal;

import com.warehouse.terminal.event.AgentEvent;

public interface DeviceEventPublisher {
    void send(final AgentEvent agentEvent);
}
