package com.warehouse.process;

import com.warehouse.process.infrastructure.event.ProcessLogEvent;

public interface ProcessHubEventPublisher {

    void publish(final ProcessLogEvent event);
}
