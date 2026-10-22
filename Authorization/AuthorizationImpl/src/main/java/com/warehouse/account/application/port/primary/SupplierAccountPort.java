package com.warehouse.account.application.port.primary;

import com.warehouse.account.application.port.primary.command.AcceptUserInvitationCommand;
import com.warehouse.account.application.port.primary.command.ProvisionSupplierAccountCommand;
import com.warehouse.account.application.port.primary.result.UserInvitationResult;
import com.warehouse.account.domain.vo.InvitationToken;
import com.warehouse.commonassets.identificator.UserId;

public interface SupplierAccountPort {
    UserId provisionSupplierAccount(final ProvisionSupplierAccountCommand command);
    UserInvitationResult getInvitation(final InvitationToken token);
    void acceptInvitation(final AcceptUserInvitationCommand command);
}
