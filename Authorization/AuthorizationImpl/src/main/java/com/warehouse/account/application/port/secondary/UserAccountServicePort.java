package com.warehouse.account.application.port.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public interface UserAccountServicePort {
    boolean usernameExists(final String username);
    UserId createSupplierUser(final NewSupplierAccount account);
    void changePassword(final OperatorId operatorId, final UserId userId, final String rawPassword);
}
