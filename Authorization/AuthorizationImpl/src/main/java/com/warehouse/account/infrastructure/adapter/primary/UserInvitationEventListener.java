package com.warehouse.account.infrastructure.adapter.primary;

import com.warehouse.account.application.port.primary.UserInvitationPort;
import com.warehouse.account.application.port.primary.command.InviteUserCommand;
import com.warehouse.auth.domain.event.UserInvitationRequestedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserInvitationEventListener {

    private final UserInvitationPort userInvitationPort;

    public UserInvitationEventListener(final UserInvitationPort userInvitationPort) {
        this.userInvitationPort = userInvitationPort;
    }

    @EventListener
    public void handle(final UserInvitationRequestedEvent event) {
        this.userInvitationPort.invite(new InviteUserCommand(
                event.userId(),
                event.username(),
                event.firstName(),
                event.email(),
                event.language()));
    }
}
