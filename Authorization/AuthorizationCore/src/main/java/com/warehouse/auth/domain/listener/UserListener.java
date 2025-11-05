package com.warehouse.auth.domain.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.auth.domain.event.UserChangedEvent;
import com.warehouse.auth.domain.event.UserCreatedEvent;
import com.warehouse.auth.domain.port.secondary.MailServicePort;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserListener {

    private final MailServicePort mailServicePort;

    public UserListener(final MailServicePort mailServicePort) {
        this.mailServicePort = mailServicePort;
    }

    @EventListener
    public void handle(final UserCreatedEvent event) {
        logEvent(event);

    }

    private void logEvent(final UserChangedEvent event) {
        log.info("User event: {}", event.getSnapshot().toString());
    }
}
