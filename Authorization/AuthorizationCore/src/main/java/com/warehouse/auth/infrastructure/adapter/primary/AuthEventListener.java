package com.warehouse.auth.infrastructure.adapter.primary;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.auth.infrastructure.adapter.primary.event.AdminUserCommand;

@Component
@Slf4j
public class AuthEventListener {

    private final AuthenticationPort authenticationPort;

    public AuthEventListener(final AuthenticationPort authenticationPort) {
        this.authenticationPort = authenticationPort;
    }

    @EventListener
    public void handle(final AdminUserCommand command) {
        log.info("Admin user command: {}", command.toString());
    }
}
