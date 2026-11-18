package com.warehouse.onboarding.infrastructure.adapter.primary;

import com.warehouse.auth.domain.event.UserInvitationRequestedEvent;
import com.warehouse.onboarding.application.port.primary.UserOnboardingPort;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserOnboardingEventListener {

    private final UserOnboardingPort userOnboardingPort;

    public UserOnboardingEventListener(final UserOnboardingPort userOnboardingPort) {
        this.userOnboardingPort = userOnboardingPort;
    }

    @EventListener
    public void handle(final UserInvitationRequestedEvent event) {
        this.userOnboardingPort.start(event.userId());
    }
}
