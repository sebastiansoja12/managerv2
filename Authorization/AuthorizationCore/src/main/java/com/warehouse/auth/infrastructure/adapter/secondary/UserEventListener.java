package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.vo.UserSnapshot;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.auth.domain.event.UserChangedEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserEventListener {

    @EventListener
    public void handle(final UserChangedEvent event) {
        logEvent(event);
        final UserSnapshot snapshot = event.getSnapshot();

    }

    private void logEvent(final UserChangedEvent event) {
        log.info("User event: {}", event.getSnapshot().toString());
    }
}
