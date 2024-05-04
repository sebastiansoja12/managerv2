package com.warehouse.routelogger.infrastructure.adapter.secondary;

import com.warehouse.routelogger.RouteLogEvent;
import com.warehouse.routelogger.RouteLogEventPublisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Slf4j
@Component
public class RouteLogEventPublisherImpl implements RouteLogEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void send(RouteLogEvent event) {
        logEvent(event);
        eventPublisher.publishEvent(event);
    }

    private void logEvent(RouteLogEvent event) {
        log.info("Publishing event {}", event.getClass().getSimpleName());
    }
}


