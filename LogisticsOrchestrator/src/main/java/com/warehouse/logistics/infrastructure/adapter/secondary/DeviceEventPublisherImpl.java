package com.warehouse.logistics.infrastructure.adapter.secondary;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.warehouse.terminal.DeviceEventPublisher;
import com.warehouse.terminal.event.AgentEvent;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Component
public class DeviceEventPublisherImpl implements DeviceEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void send(final AgentEvent event) {
        logEvent(event);
        eventPublisher.publishEvent(event);
    }

    private void logEvent(final AgentEvent event) {
        log.info("Publishing event {}", event.getClass().getSimpleName());
    }
}
