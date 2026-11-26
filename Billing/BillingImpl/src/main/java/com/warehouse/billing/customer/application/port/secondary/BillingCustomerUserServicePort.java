package com.warehouse.billing.customer.application.port.secondary;

import com.warehouse.billing.customer.domain.vo.BillingCustomerAccount;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public interface BillingCustomerUserServicePort {

    BillingCustomerAccount createUser(final String username, final String name, final String email);

    String regenerateApiKey(final UserId userId);

    UserId currentUserId();

    OperatorId currentOperatorId();
}
