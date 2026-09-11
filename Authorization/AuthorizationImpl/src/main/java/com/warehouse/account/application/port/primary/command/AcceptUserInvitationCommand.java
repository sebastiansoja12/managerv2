package com.warehouse.account.application.port.primary.command;

import com.warehouse.account.domain.vo.InvitationToken;

public record AcceptUserInvitationCommand(InvitationToken token, String password) {
}
