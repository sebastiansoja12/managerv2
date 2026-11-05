package com.warehouse.account.application.port.primary;

import com.warehouse.account.application.port.primary.command.InviteUserCommand;

public interface UserInvitationPort {
    void invite(final InviteUserCommand command);
}
