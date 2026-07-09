package com.warehouse.process.infrastructure.adapter.secondary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import com.warehouse.process.ProcessHubEventPublisher;
import com.warehouse.process.infrastructure.event.ProcessLogEvent;

public class ProcessHubEventPublisherImpl implements ProcessHubEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(ProcessHubEventPublisherImpl.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public ProcessHubEventPublisherImpl(final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(final ProcessLogEvent event) {
        log.info("Publishing ProcessHub event {}", event.getClass().getSimpleName());
        this.applicationEventPublisher.publishEvent(event);
    }
}
